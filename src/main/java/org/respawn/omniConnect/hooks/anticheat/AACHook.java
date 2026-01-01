package org.respawn.omniConnect.hooks.anticheat;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class AACHook implements Listener {

    private final String pluginKey;

    public AACHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onViolation(Event event) {
        if (!event.getClass().getName().equals("me.konsolas.aac.api.PlayerViolationEvent")) return;

        try {
            Object player = event.getClass().getMethod("getPlayer").invoke(event);
            Object hackType = event.getClass().getMethod("getHackType").invoke(event);
            int vl = (int) event.getClass().getMethod("getViolations").invoke(event);
            String playerName = (String) player.getClass().getMethod("getName").invoke(player);

            DiscordLog.send(pluginKey,
                    "⚠️ AAC riasztás",
                    "Játékos: **" + playerName + "**\nCheck: **" + hackType.toString() + "**\nVL: **" + vl + "**"
            );
        } catch (Exception ignored) {}
    }
}
