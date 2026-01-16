package org.respawn.omniConnect.hooks.moderation;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class StaffPlusHook implements Listener {

    private final String pluginKey;

    public StaffPlusHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onStaffPlus(Event event) {
        String name = event.getClass().getName();

        try {
            String lang = lang();

            switch (name) {

                case "net.shortninja.staffplus.core.events.PlayerReportEvent": {
                    Object reporter = event.getClass().getMethod("getReporter").invoke(event);
                    Object target = event.getClass().getMethod("getReported").invoke(event);
                    Object reason = event.getClass().getMethod("getReason").invoke(event);

                    String reporterName = (String) reporter.getClass().getMethod("getName").invoke(reporter);
                    String targetName = (String) target.getClass().getMethod("getName").invoke(target);

                    String title = LangManager.get(lang, "hooks.moderation.staffplus.log.report.title");

                    String body =
                            LangManager.get(lang, "hooks.moderation.staffplus.log.report.reporter") + ": **" + reporterName + "**\n" +
                                    LangManager.get(lang, "hooks.moderation.staffplus.log.report.target") + ": **" + targetName + "**\n" +
                                    LangManager.get(lang, "hooks.moderation.staffplus.log.report.reason") + ": **" + reason.toString() + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                default:
                    break;
            }

        } catch (Exception ignored) {}
    }
}
