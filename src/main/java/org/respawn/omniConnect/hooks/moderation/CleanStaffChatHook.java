package org.respawn.omniConnect.hooks.moderation;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class CleanStaffChatHook implements Listener {

    private final String pluginKey;

    public CleanStaffChatHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] CleanStaffChat hook aktiválva!");
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onCleanStaffChatEvent(Event event) {
        String name = event.getClass().getName();

        try {
            String lang = lang();

            // ------------------------------------------------------------
            // StaffChatMessageEvent
            // ------------------------------------------------------------
            if (name.equals("me.lucko.cleanstaffchat.api.event.StaffChatMessageEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                Object message = event.getClass().getMethod("getMessage").invoke(event);

                String title = LangManager.get(lang, "hooks.moderation.cleanstaffchat.log.message.title");
                String body =
                        LangManager.get(lang, "hooks.moderation.cleanstaffchat.log.message.player") + ": **" + player + "**\n" +
                                LangManager.get(lang, "hooks.moderation.cleanstaffchat.log.message.message") + ": `" + message + "`";

                DiscordLog.send(pluginKey, title, body);
            }

            // ------------------------------------------------------------
            // StaffChatToggleEvent (ha létezik)
            // ------------------------------------------------------------
            if (name.equals("me.lucko.cleanstaffchat.api.event.StaffChatToggleEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                Object state = event.getClass().getMethod("isEnabled").invoke(event);

                String title = LangManager.get(lang, "hooks.moderation.cleanstaffchat.log.toggle.title");
                String body =
                        LangManager.get(lang, "hooks.moderation.cleanstaffchat.log.toggle.player") + ": **" + player + "**\n" +
                                LangManager.get(lang, "hooks.moderation.cleanstaffchat.log.toggle.state") + ": **" + state + "**";

                DiscordLog.send(pluginKey, title, body);
            }

        } catch (Exception ignored) {}
    }
}
