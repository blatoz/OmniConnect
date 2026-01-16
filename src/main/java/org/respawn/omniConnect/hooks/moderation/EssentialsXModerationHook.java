package org.respawn.omniConnect.hooks.moderation;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class EssentialsXModerationHook implements Listener {

    private final String pluginKey;

    public EssentialsXModerationHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onEssentialsModeration(Event event) {
        String name = event.getClass().getName();

        try {
            String lang = lang();

            switch (name) {

                case "com.earth2me.essentials.events.EssentialsBanEvent": {
                    Object ban = event.getClass().getMethod("getBan").invoke(event);
                    String target = (String) ban.getClass().getMethod("getName").invoke(ban);

                    String title = LangManager.get(lang, "hooks.moderation.essentialsx.log.ban.title");
                    String body = LangManager.get(lang, "hooks.moderation.essentialsx.log.ban.player") + ": **" + target + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                case "com.earth2me.essentials.events.EssentialsTempBanEvent": {
                    Object ban = event.getClass().getMethod("getBan").invoke(event);
                    String target = (String) ban.getClass().getMethod("getName").invoke(ban);

                    String title = LangManager.get(lang, "hooks.moderation.essentialsx.log.tempban.title");
                    String body = LangManager.get(lang, "hooks.moderation.essentialsx.log.tempban.player") + ": **" + target + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                case "com.earth2me.essentials.events.EssentialsMuteEvent": {
                    Object mute = event.getClass().getMethod("getMute").invoke(event);
                    String target = (String) mute.getClass().getMethod("getName").invoke(mute);

                    String title = LangManager.get(lang, "hooks.moderation.essentialsx.log.mute.title");
                    String body = LangManager.get(lang, "hooks.moderation.essentialsx.log.mute.player") + ": **" + target + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                case "com.earth2me.essentials.events.EssentialsUnmuteEvent": {
                    Object mute = event.getClass().getMethod("getMute").invoke(event);
                    String target = (String) mute.getClass().getMethod("getName").invoke(mute);

                    String title = LangManager.get(lang, "hooks.moderation.essentialsx.log.unmute.title");
                    String body = LangManager.get(lang, "hooks.moderation.essentialsx.log.unmute.player") + ": **" + target + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                case "com.earth2me.essentials.events.EssentialsKickEvent": {
                    Object kicked = event.getClass().getMethod("getKicked").invoke(event);
                    String target = (String) kicked.getClass().getMethod("getName").invoke(kicked);

                    String title = LangManager.get(lang, "hooks.moderation.essentialsx.log.kick.title");
                    String body = LangManager.get(lang, "hooks.moderation.essentialsx.log.kick.player") + ": **" + target + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                default:
                    break;
            }

        } catch (Exception ignored) {}
    }
}
