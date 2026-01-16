package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class AutoRestartHook implements Listener {

    private final String pluginKey;

    public AutoRestartHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] AutoRestart hook has been enabled!");
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String msg = event.getMessage();
        if (!isRestartMessage(msg)) return;

        String lang = lang();

        String title = LangManager.get(lang, "hooks.management.autorestart.log.restart.title");
        String body = LangManager.get(lang, "hooks.management.autorestart.log.restart.message") + ": `" + msg + "`";

        DiscordLog.send(pluginKey, title, body);
    }

    private boolean isRestartMessage(String msg) {
        if (msg == null) return false;

        String m = msg.toLowerCase();

        return m.contains("restarting")
                || m.contains("server restarting")
                || m.contains("restart in")
                || m.contains("auto restart")
                || m.contains("autorestart")
                || m.contains("restart scheduled")
                || m.contains("server will restart");
    }
}
