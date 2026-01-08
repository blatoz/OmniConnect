package org.respawn.omniConnect.hooks.moderation;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class ChatControlHook implements Listener {

    private final String pluginKey;

    public ChatControlHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onChatControl(Event event) {
        String name = event.getClass().getName();

        try {
            switch (name) {

                case "com.zrips.chatcontrol.events.RuleMatchEvent": {
                    Object player = event.getClass().getMethod("getPlayer").invoke(event);
                    Object rule = event.getClass().getMethod("getRule").invoke(event);
                    Object message = event.getClass().getMethod("getMessage").invoke(event);

                    String playerName = (String) player.getClass().getMethod("getName").invoke(player);
                    String ruleName = rule.toString();
                    String msg = message.toString();

                    DiscordLog.send(pluginKey,
                            "🛑 ChatControl Szabálytalanság",
                            "Játékos: **" + playerName + "**\n"
                                    + "Szabály: **" + ruleName + "**\n"
                                    + "Üzenet: **" + msg + "**"
                    );
                    break;
                }

                default:
                    break;
            }

        } catch (Exception ignored) {}
    }
}
