package org.respawn.omniConnect.hooks.moderation;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class EssentialsJailHook implements Listener {

    private final String pluginKey;

    public EssentialsJailHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onEssentialsJail(Event event) {
        String name = event.getClass().getName();

        try {
            String lang = lang();

            switch (name) {

                case "com.earth2me.essentials.events.EssentialsJailEvent": {
                    Object player = event.getClass().getMethod("getAffected").invoke(event);
                    String target = (String) player.getClass().getMethod("getName").invoke(player);

                    String title = LangManager.get(lang, "hooks.moderation.essentialsjail.log.jail.title");
                    String body = LangManager.get(lang, "hooks.moderation.essentialsjail.log.jail.player") + ": **" + target + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                case "com.earth2me.essentials.events.EssentialsUnjailEvent": {
                    Object player = event.getClass().getMethod("getAffected").invoke(event);
                    String target = (String) player.getClass().getMethod("getName").invoke(player);

                    String title = LangManager.get(lang, "hooks.moderation.essentialsjail.log.unjail.title");
                    String body = LangManager.get(lang, "hooks.moderation.essentialsjail.log.unjail.player") + ": **" + target + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                default:
                    break;
            }

        } catch (Exception ignored) {}
    }
}
