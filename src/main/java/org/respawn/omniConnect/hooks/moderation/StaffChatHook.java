package org.respawn.omniConnect.hooks.moderation;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class StaffChatHook implements Listener {

    private final String pluginKey;

    public StaffChatHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onStaffChat(Event event) {
        if (!event.getClass().getName().equals("com.staffchat.api.StaffChatEvent"))
            return;

        try {
            String lang = lang();

            Object player = event.getClass().getMethod("getPlayer").invoke(event);
            Object message = event.getClass().getMethod("getMessage").invoke(event);

            String playerName = (String) player.getClass().getMethod("getName").invoke(player);
            String msg = message.toString();

            String title = LangManager.get(lang, "hooks.moderation.staffchat.log.message.title");

            String body =
                    "**" + playerName + "**: " +
                            LangManager.get(lang, "hooks.moderation.staffchat.log.message.message") +
                            " **" + msg + "**";

            DiscordLog.send(pluginKey, title, body);

        } catch (Exception ignored) {}
    }
}
