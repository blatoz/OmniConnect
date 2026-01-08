package org.respawn.omniConnect.hooks.anticheat;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;


public class MatrixHook implements Listener {

    private final String pluginKey;

    public MatrixHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onViolation(Event event) {
        if (!event.getClass().getName().equals("me.rerere.matrix.api.events.PlayerViolationEvent")) return;

        try {
            Object player = event.getClass().getMethod("getPlayer").invoke(event);
            Object hackType = event.getClass().getMethod("getHackType").invoke(event);
            String playerName = (String) player.getClass().getMethod("getName").invoke(player);

            DiscordLog.send(pluginKey,
                    "⚠️ Matrix Riasztás",
                    "Játékos: **" + playerName + "**\nHack Típus: **" + hackType.toString() + "**"
            );
        } catch (Exception ignored) {}
    }
}
