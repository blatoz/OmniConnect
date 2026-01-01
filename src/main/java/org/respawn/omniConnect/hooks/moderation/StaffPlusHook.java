package org.respawn.omniConnect.hooks.moderation;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class StaffPlusHook implements Listener {

    private final String pluginKey;

    public StaffPlusHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onStaffPlus(Event event) {
        String name = event.getClass().getName();

        try {
            switch (name) {

                case "net.shortninja.staffplus.core.events.PlayerReportEvent": {
                    Object reporter = event.getClass().getMethod("getReporter").invoke(event);
                    Object target = event.getClass().getMethod("getReported").invoke(event);
                    Object reason = event.getClass().getMethod("getReason").invoke(event);

                    String reporterName = (String) reporter.getClass().getMethod("getName").invoke(reporter);
                    String targetName = (String) target.getClass().getMethod("getName").invoke(target);

                    DiscordLog.send(pluginKey,
                            "📣 StaffPlus Report",
                            "Jelentő: **" + reporterName + "**\n"
                                    + "Célpont: **" + targetName + "**\n"
                                    + "Indok: **" + reason.toString() + "**"
                    );
                    break;
                }

                default:
                    break;
            }

        } catch (Exception ignored) {}
    }
}
