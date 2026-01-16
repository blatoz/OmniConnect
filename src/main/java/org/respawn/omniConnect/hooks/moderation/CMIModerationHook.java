package org.respawn.omniConnect.hooks.moderation;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class CMIModerationHook implements Listener {

    private final String pluginKey;

    public CMIModerationHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onCMIEvent(Event event) {
        String name = event.getClass().getName();

        try {
            String lang = lang();

            switch (name) {

                case "com.Zrips.CMI.events.CMIBanEvent": {
                    Object banned = event.getClass().getMethod("getBanned").invoke(event);
                    String target = (String) banned.getClass().getMethod("getName").invoke(banned);

                    String title = LangManager.get(lang, "hooks.moderation.cmi.log.ban.title");
                    String body = LangManager.get(lang, "hooks.moderation.cmi.log.ban.player") + ": **" + target + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                case "com.Zrips.CMI.events.CMIMuteEvent": {
                    Object muted = event.getClass().getMethod("getMuted").invoke(event);
                    String target = (String) muted.getClass().getMethod("getName").invoke(muted);

                    String title = LangManager.get(lang, "hooks.moderation.cmi.log.mute.title");
                    String body = LangManager.get(lang, "hooks.moderation.cmi.log.mute.player") + ": **" + target + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                case "com.Zrips.CMI.events.CMIKickEvent": {
                    Object kicked = event.getClass().getMethod("getKicked").invoke(event);
                    String target = (String) kicked.getClass().getMethod("getName").invoke(kicked);

                    String title = LangManager.get(lang, "hooks.moderation.cmi.log.kick.title");
                    String body = LangManager.get(lang, "hooks.moderation.cmi.log.kick.player") + ": **" + target + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                case "com.Zrips.CMI.events.CMIJailEvent": {
                    Object jailed = event.getClass().getMethod("getJailed").invoke(event);
                    String target = (String) jailed.getClass().getMethod("getName").invoke(jailed);

                    String title = LangManager.get(lang, "hooks.moderation.cmi.log.jail.title");
                    String body = LangManager.get(lang, "hooks.moderation.cmi.log.jail.player") + ": **" + target + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                case "com.Zrips.CMI.events.CMIUnjailEvent": {
                    Object jailed = event.getClass().getMethod("getJailed").invoke(event);
                    String target = (String) jailed.getClass().getMethod("getName").invoke(jailed);

                    String title = LangManager.get(lang, "hooks.moderation.cmi.log.unjail.title");
                    String body = LangManager.get(lang, "hooks.moderation.cmi.log.unjail.player") + ": **" + target + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                default:
                    break;
            }

        } catch (Exception ignored) {}
    }
}
