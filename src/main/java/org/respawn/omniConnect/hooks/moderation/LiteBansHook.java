package org.respawn.omniConnect.hooks.moderation;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class LiteBansHook implements Listener {

    private final String pluginKey;

    public LiteBansHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onPunishment(Event event) {
        if (!event.getClass().getName().equals("litebans.api.EventPunishmentAdd"))
            return;

        try {
            Object punishment = event.getClass().getMethod("getPunishment").invoke(event);

            String type = (String) punishment.getClass().getMethod("getType").invoke(punishment);
            String target = (String) punishment.getClass().getMethod("getName").invoke(punishment);
            String executor = (String) punishment.getClass().getMethod("getExecutorName").invoke(punishment);
            String reason = (String) punishment.getClass().getMethod("getReason").invoke(punishment);

            DiscordLog.send(pluginKey,
                    "🔨 LiteBans büntetés",
                    "Típus: **" + type + "**\n"
                            + "Játékos: **" + target + "**\n"
                            + "Staff: **" + executor + "**\n"
                            + "Indok: **" + reason + "**"
            );

        } catch (Exception ignored) {}
    }
}
