package org.respawn.omniConnect.hooks.moderation;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class StaffChatHook implements Listener {

    private final String pluginKey;

    public StaffChatHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onStaffChat(Event event) {
        if (!event.getClass().getName().equals("com.staffchat.api.StaffChatEvent"))
            return;

        try {
            Object player = event.getClass().getMethod("getPlayer").invoke(event);
            Object message = event.getClass().getMethod("getMessage").invoke(event);

            String playerName = (String) player.getClass().getMethod("getName").invoke(player);
            String msg = message.toString();

            DiscordLog.send(pluginKey,
                    "🛡️ StaffChat",
                    "**" + playerName + "**: " + msg
            );

        } catch (Exception ignored) {}
    }
}
