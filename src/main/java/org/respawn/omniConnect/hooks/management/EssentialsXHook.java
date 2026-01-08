package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class EssentialsXHook implements Listener {

    private final String pluginKey;

    public EssentialsXHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] EssentialsX hook aktiválva!");
    }

    // ------------------------------------------------------------
    // Játékos parancsok figyelése
    // ------------------------------------------------------------
    @EventHandler
    public void onPlayerEssentialsCommand(PlayerCommandPreprocessEvent event) {
        String msg = event.getMessage().toLowerCase();
        if (!isLoggedEssentialsCommand(msg)) return;

        String player = event.getPlayer().getName();

        DiscordLog.send(
                pluginKey,
                "📘 EssentialsX – Parancs Végrehajtva (Játékos)",
                "Játékos: **" + player + "**\n"
                        + "Parancs: `" + msg + "`"
        );
    }

    // ------------------------------------------------------------
    // Konzol parancsok figyelése
    // ------------------------------------------------------------
    @EventHandler
    public void onConsoleEssentialsCommand(ServerCommandEvent event) {
        String msg = "/" + event.getCommand().toLowerCase();
        if (!isLoggedEssentialsCommand(msg)) return;

        DiscordLog.send(
                pluginKey,
                "📘 EssentialsX – Parancs Végrehajtva (Konzol)",
                "Játékos: **CONSOLE**\n"
                        + "Parancs: `" + msg + "`"
        );
    }

    // ------------------------------------------------------------
    // Csak az általad kért parancsok logolása
    // ------------------------------------------------------------
    private boolean isLoggedEssentialsCommand(String cmd) {
        return cmd.startsWith("/invsee")
                || cmd.startsWith("/tp")
                || cmd.startsWith("/tphere")
                || cmd.startsWith("/tppos")
                || cmd.startsWith("/tpall")
                || cmd.startsWith("/god")
                || cmd.startsWith("/speed")
                || cmd.startsWith("/flyspeed")
                || cmd.startsWith("/rules")
                || cmd.startsWith("/motd")
                || cmd.startsWith("/near")
                || cmd.startsWith("/depth")
                || cmd.startsWith("/itemdb")
                || cmd.startsWith("/getpos")
                || cmd.startsWith("/pinfo")
                || cmd.startsWith("/whois")
                || cmd.startsWith("/uptime")
                || cmd.startsWith("/seen")
                || cmd.startsWith("/gc")
                || cmd.startsWith("/lag");
    }
}
