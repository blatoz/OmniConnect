package org.respawn.omniConnect.hooks.moderation;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class ChatControlRedHook implements Listener {

    private final String pluginKey;

    public ChatControlRedHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onRuleMatch(Event event) {
        if (!event.getClass().getName().equals("com.github.simplixsoft.chatrecontrol.api.events.RuleMatchEvent"))
            return;

        try {
            String lang = lang();

            Object player = event.getClass().getMethod("getPlayer").invoke(event);
            Object rule = event.getClass().getMethod("getRule").invoke(event);
            Object message = event.getClass().getMethod("getMessage").invoke(event);

            String playerName = (String) player.getClass().getMethod("getName").invoke(player);

            String title = LangManager.get(lang, "hooks.moderation.chatcontrolred.log.rule_match.title");

            String body =
                    LangManager.get(lang, "hooks.moderation.chatcontrolred.log.rule_match.player") + ": **" + playerName + "**\n" +
                            LangManager.get(lang, "hooks.moderation.chatcontrolred.log.rule_match.rule") + ": **" + rule.toString() + "**\n" +
                            LangManager.get(lang, "hooks.moderation.chatcontrolred.log.rule_match.message") + ": **" + message.toString() + "**";

            DiscordLog.send(pluginKey, title, body);

        } catch (Exception ignored) {}
    }
}
