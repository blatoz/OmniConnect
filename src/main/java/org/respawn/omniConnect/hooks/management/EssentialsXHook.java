package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class EssentialsXHook implements Listener {

    private final String pluginKey;

    public EssentialsXHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] EssentialsX hook has been enabled!");
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onPlayerEssentialsCommand(PlayerCommandPreprocessEvent event) {
        String msg = event.getMessage().toLowerCase();
        if (!isLoggedEssentialsCommand(msg)) return;

        String lang = lang();
        String player = event.getPlayer().getName();

        String title = LangManager.get(lang, "hooks.management.essentialsx.log.player_command.title");
        String body =
                LangManager.get(lang, "hooks.management.essentialsx.log.player_command.executor") + ": **" + player + "**\n" +
                        LangManager.get(lang, "hooks.management.essentialsx.log.player_command.command") + ": `" + msg + "`";

        DiscordLog.send(pluginKey, title, body);
    }

    @EventHandler
    public void onConsoleEssentialsCommand(ServerCommandEvent event) {
        String msg = "/" + event.getCommand().toLowerCase();
        if (!isLoggedEssentialsCommand(msg)) return;

        String lang = lang();

        String title = LangManager.get(lang, "hooks.management.essentialsx.log.console_command.title");
        String body =
                LangManager.get(lang, "hooks.management.essentialsx.log.console_command.executor") + ": **CONSOLE**\n" +
                        LangManager.get(lang, "hooks.management.essentialsx.log.console_command.command") + ": `" + msg + "`";

        DiscordLog.send(pluginKey, title, body);
    }

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
