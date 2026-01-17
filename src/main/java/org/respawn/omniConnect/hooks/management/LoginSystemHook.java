package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class LoginSystemHook implements Listener {

    private final String pluginKey;

    public LoginSystemHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] LoginSystem hook has been enabled!");
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onLoginSystemEvent(Event event) {
        String name = event.getClass().getName();

        try {
            String lang = lang();

            // PlayerLoginEvent
            if (name.equals("me.bartuabi.loginsystem.api.events.PlayerLoginEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);

                DiscordLog.send(pluginKey,
                        LangManager.get(lang, "hooks.management.loginsystem.log.login.title"),
                        LangManager.get(lang, "hooks.management.loginsystem.log.login.player") + ": **" + player + "**"
                );
            }

            // PlayerRegisterEvent
            if (name.equals("me.bartuabi.loginsystem.api.events.PlayerRegisterEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);

                DiscordLog.send(pluginKey,
                        LangManager.get(lang, "hooks.management.loginsystem.log.register.title"),
                        LangManager.get(lang, "hooks.management.loginsystem.log.register.player") + ": **" + player + "**"
                );
            }

            // PlayerLogoutEvent
            if (name.equals("me.bartuabi.loginsystem.api.events.PlayerLogoutEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);

                DiscordLog.send(pluginKey,
                        LangManager.get(lang, "hooks.management.loginsystem.log.logout.title"),
                        LangManager.get(lang, "hooks.management.loginsystem.log.logout.player") + ": **" + player + "**"
                );
            }

            // PlayerPasswordChangeEvent
            if (name.equals("me.bartuabi.loginsystem.api.events.PlayerPasswordChangeEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);

                DiscordLog.send(pluginKey,
                        LangManager.get(lang, "hooks.management.loginsystem.log.password_change.title"),
                        LangManager.get(lang, "hooks.management.loginsystem.log.password_change.player") + ": **" + player + "**"
                );
            }

            // PlayerSessionStartEvent
            if (name.equals("me.bartuabi.loginsystem.api.events.PlayerSessionStartEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                Object method = event.getClass().getMethod("getMethod").invoke(event);

                DiscordLog.send(pluginKey,
                        LangManager.get(lang, "hooks.management.loginsystem.log.session_start.title"),
                        LangManager.get(lang, "hooks.management.loginsystem.log.session_start.player") + ": **" + player + "**\n" +
                                LangManager.get(lang, "hooks.management.loginsystem.log.session_start.method") + ": **" + method + "**"
                );
            }

            // PlayerSessionEndEvent
            if (name.equals("me.bartuabi.loginsystem.api.events.PlayerSessionEndEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                Object reason = event.getClass().getMethod("getReason").invoke(event);

                DiscordLog.send(pluginKey,
                        LangManager.get(lang, "hooks.management.loginsystem.log.session_end.title"),
                        LangManager.get(lang, "hooks.management.loginsystem.log.session_end.player") + ": **" + player + "**\n" +
                                LangManager.get(lang, "hooks.management.loginsystem.log.session_end.reason") + ": **" + reason + "**"
                );
            }

        } catch (Exception ignored) {}
    }
}
