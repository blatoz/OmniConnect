package org.respawn.omniConnect.hooks.moderation;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class ChatControlHook implements Listener {

    private final String pluginKey;

    public ChatControlHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onChatControl(Event event) {
        String name = event.getClass().getName();

        try {
            String lang = lang();

            switch (name) {

                case "com.zrips.chatcontrol.events.RuleMatchEvent": {
                    Object player = event.getClass().getMethod("getPlayer").invoke(event);
                    Object rule = event.getClass().getMethod("getRule").invoke(event);
                    Object message = event.getClass().getMethod("getMessage").invoke(event);

                    String playerName = (String) player.getClass().getMethod("getName").invoke(player);
                    String ruleName = rule.toString();
                    String msg = message.toString();

                    String title = LangManager.get(lang, "hooks.moderation.chatcontrol.log.rule_match.title");

                    String body =
                            LangManager.get(lang, "hooks.moderation.chatcontrol.log.rule_match.player") + ": **" + playerName + "**\n" +
                                    LangManager.get(lang, "hooks.moderation.chatcontrol.log.rule_match.rule") + ": **" + ruleName + "**\n" +
                                    LangManager.get(lang, "hooks.moderation.chatcontrol.log.rule_match.message") + ": **" + msg + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                default:
                    break;
            }

        } catch (Exception ignored) {}
    }
}
