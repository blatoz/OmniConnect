package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class CoreProtectHook implements Listener {

    private final String pluginKey;

    public CoreProtectHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] CoreProtect hook aktiválva!");
    }

    @EventHandler
    public void onPlayerCoCommand(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
        if (!isCoreProtectCommand(message)) {
            return;
        }

        String playerName = event.getPlayer().getName();

        DiscordLog.send(
                pluginKey,
                "🧩 CoreProtect – Parancs Végrehajtva (Játékos)",
                "Végrehajtó: **" + playerName + "**\n"
                        + "Parancs: `" + message + "`"
        );
    }

    @EventHandler
    public void onConsoleCoCommand(ServerCommandEvent event) {
        String command = event.getCommand();
        if (!isCoreProtectCommand("/" + command)) {
            return;
        }

        DiscordLog.send(
                pluginKey,
                "🧩 CoreProtect – Parancs Végrehajtva (Konzol)",
                "Végrehajtó: **CONSOLE**\n"
                        + "Parancs: `/" + command + "`"
        );
    }

    private boolean isCoreProtectCommand(String raw) {
        if (raw == null) return false;

        String msg = raw.trim().toLowerCase();
        // /co, /co rollback, /co restore, stb.
        if (!msg.startsWith("/co")) return false;

        // opcionálisan kizárhatnánk aliasokat, ha zavarnak
        return true;
    }
}
