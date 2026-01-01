package org.respawn.omniConnect.hooks.moderation;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class LibertyBansHook implements Listener {

    private final String pluginKey;

    public LibertyBansHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onLibertyBans(Event event) {
        if (!event.getClass().getName().equals("space.arim.libertybans.api.event.PunishmentEvent"))
            return;

        try {
            Object punishment = event.getClass().getMethod("getPunishment").invoke(event);

            String type = punishment.getClass().getMethod("getType").invoke(punishment).toString();
            String target = punishment.getClass().getMethod("getTargetName").invoke(punishment).toString();
            String operator = punishment.getClass().getMethod("getOperatorName").invoke(punishment).toString();
            String reason = punishment.getClass().getMethod("getReason").invoke(punishment).toString();

            DiscordLog.send(pluginKey,
                    "🧨 LibertyBans büntetés",
                    "Típus: **" + type + "**\n"
                            + "Játékos: **" + target + "**\n"
                            + "Staff: **" + operator + "**\n"
                            + "Indok: **" + reason + "**"
            );

        } catch (Exception ignored) {}
    }
}
