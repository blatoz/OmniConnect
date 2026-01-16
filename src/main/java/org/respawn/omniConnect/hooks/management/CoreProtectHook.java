package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class CoreProtectHook implements Listener {

    private final String pluginKey;

    public CoreProtectHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] CoreProtect hook has been enabled!");
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onPlayerCoCommand(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
        if (!isCoreProtectCommand(message)) return;

        String lang = lang();
        String playerName = event.getPlayer().getName();

        String title = LangManager.get(lang, "hooks.management.coreprotect.log.player_command.title");
        String body =
                LangManager.get(lang, "hooks.management.coreprotect.log.player_command.executor") + ": **" + playerName + "**\n" +
                        LangManager.get(lang, "hooks.management.coreprotect.log.player_command.command") + ": `" + message + "`";

        DiscordLog.send(pluginKey, title, body);
    }

    @EventHandler
    public void onConsoleCoCommand(ServerCommandEvent event) {
        String command = event.getCommand();
        if (!isCoreProtectCommand("/" + command)) return;

        String lang = lang();

        String title = LangManager.get(lang, "hooks.management.coreprotect.log.console_command.title");
        String body =
                LangManager.get(lang, "hooks.management.coreprotect.log.console_command.executor") + ": **CONSOLE**\n" +
                        LangManager.get(lang, "hooks.management.coreprotect.log.console_command.command") + ": `/" + command + "`";

        DiscordLog.send(pluginKey, title, body);
    }

    private boolean isCoreProtectCommand(String raw) {
        if (raw == null) return false;

        String msg = raw.trim().toLowerCase();
        return msg.startsWith("/co");
    }
}
