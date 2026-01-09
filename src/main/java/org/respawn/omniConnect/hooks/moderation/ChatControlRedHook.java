package org.respawn.omniConnect.hooks.moderation;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class ChatControlRedHook implements Listener {

    private final String pluginKey;

    public ChatControlRedHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onRuleMatch(Event event) {
        if (!event.getClass().getName().equals("com.github.simplixsoft.chatrecontrol.api.events.RuleMatchEvent"))
            return;

        try {
            Object player = event.getClass().getMethod("getPlayer").invoke(event);
            Object rule = event.getClass().getMethod("getRule").invoke(event);
            Object message = event.getClass().getMethod("getMessage").invoke(event);

            String playerName = (String) player.getClass().getMethod("getName").invoke(player);

            DiscordLog.send(pluginKey,
                    "🛑 ChatControl Red Szabálytalanság",
                    "Játékos: **" + playerName + "**\n"
                            + "Szabály: **" + rule.toString() + "**\n"
                            + "Üzenet: **" + message.toString() + "**"
            );

        } catch (Exception ignored) {}
    }
}
