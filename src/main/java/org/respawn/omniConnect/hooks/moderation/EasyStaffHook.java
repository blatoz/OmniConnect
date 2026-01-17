package org.respawn.omniConnect.hooks.moderation;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class EasyStaffHook implements Listener {

    private final String pluginKey;

    public EasyStaffHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] EasyStaff hook has been enabled!");
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onEasyStaffEvent(Event event) {
        String name = event.getClass().getName();

        try {
            String lang = lang();

            // StaffChatEvent
            if (name.equals("me.easystaff.api.events.StaffChatEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                Object message = event.getClass().getMethod("getMessage").invoke(event);

                String title = LangManager.get(lang, "hooks.moderation.easystaff.log.chat.title");
                String body =
                        LangManager.get(lang, "hooks.moderation.easystaff.log.chat.player") + ": **" + player + "**\n" +
                                LangManager.get(lang, "hooks.moderation.easystaff.log.chat.message") + ": `" + message + "`";

                DiscordLog.send(pluginKey, title, body);
            }

            // StaffModeEnableEvent
            if (name.equals("me.easystaff.api.events.StaffModeEnableEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);

                String title = LangManager.get(lang, "hooks.moderation.easystaff.log.mode_enable.title");
                String body =
                        LangManager.get(lang, "hooks.moderation.easystaff.log.mode_enable.player") + ": **" + player + "**";

                DiscordLog.send(pluginKey, title, body);
            }

            // StaffModeDisableEvent
            if (name.equals("me.easystaff.api.events.StaffModeDisableEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);

                String title = LangManager.get(lang, "hooks.moderation.easystaff.log.mode_disable.title");
                String body =
                        LangManager.get(lang, "hooks.moderation.easystaff.log.mode_disable.player") + ": **" + player + "**";

                DiscordLog.send(pluginKey, title, body);
            }

            // StaffVanishToggleEvent
            if (name.equals("me.easystaff.api.events.StaffVanishToggleEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                Object state = event.getClass().getMethod("isVanished").invoke(event);

                String title = LangManager.get(lang, "hooks.moderation.easystaff.log.vanish_toggle.title");
                String body =
                        LangManager.get(lang, "hooks.moderation.easystaff.log.vanish_toggle.player") + ": **" + player + "**\n" +
                                LangManager.get(lang, "hooks.moderation.easystaff.log.vanish_toggle.state") + ": **" + state + "**";

                DiscordLog.send(pluginKey, title, body);
            }

            // StaffFreezeEvent
            if (name.equals("me.easystaff.api.events.StaffFreezeEvent")) {

                Object staff = event.getClass().getMethod("getStaff").invoke(event);
                Object target = event.getClass().getMethod("getTarget").invoke(event);

                String title = LangManager.get(lang, "hooks.moderation.easystaff.log.freeze.title");
                String body =
                        LangManager.get(lang, "hooks.moderation.easystaff.log.freeze.staff") + ": **" + staff + "**\n" +
                                LangManager.get(lang, "hooks.moderation.easystaff.log.freeze.target") + ": **" + target + "**";

                DiscordLog.send(pluginKey, title, body);
            }

            // StaffUnfreezeEvent
            if (name.equals("me.easystaff.api.events.StaffUnfreezeEvent")) {

                Object staff = event.getClass().getMethod("getStaff").invoke(event);
                Object target = event.getClass().getMethod("getTarget").invoke(event);

                String title = LangManager.get(lang, "hooks.moderation.easystaff.log.unfreeze.title");
                String body =
                        LangManager.get(lang, "hooks.moderation.easystaff.log.unfreeze.staff") + ": **" + staff + "**\n" +
                                LangManager.get(lang, "hooks.moderation.easystaff.log.unfreeze.target") + ": **" + target + "**";

                DiscordLog.send(pluginKey, title, body);
            }

        } catch (Exception ignored) {}
    }
}
