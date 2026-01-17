package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class LibreLoginHook implements Listener {

    private final String pluginKey;

    public LibreLoginHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] LibreLogin hook has been enabled!");
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onLibreLoginEvent(Event event) {
        String name = event.getClass().getName();

        try {
            String lang = lang();

            // LoginEvent
            if (name.equals("com.github.librelogin.api.events.LoginEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);

                DiscordLog.send(
                        pluginKey,
                        LangManager.get(lang, "hooks.management.librelogin.log.login.title"),
                        LangManager.get(lang, "hooks.management.librelogin.log.login.player") + ": **" + player + "**"
                );
            }

            // RegisterEvent
            if (name.equals("com.github.librelogin.api.events.RegisterEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);

                DiscordLog.send(
                        pluginKey,
                        LangManager.get(lang, "hooks.management.librelogin.log.register.title"),
                        LangManager.get(lang, "hooks.management.librelogin.log.register.player") + ": **" + player + "**"
                );
            }

            // LogoutEvent
            if (name.equals("com.github.librelogin.api.events.LogoutEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);

                DiscordLog.send(
                        pluginKey,
                        LangManager.get(lang, "hooks.management.librelogin.log.logout.title"),
                        LangManager.get(lang, "hooks.management.librelogin.log.logout.player") + ": **" + player + "**"
                );
            }

            // SessionStartEvent
            if (name.equals("com.github.librelogin.api.events.SessionStartEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                Object method = event.getClass().getMethod("getMethod").invoke(event);

                DiscordLog.send(
                        pluginKey,
                        LangManager.get(lang, "hooks.management.librelogin.log.session_start.title"),
                        LangManager.get(lang, "hooks.management.librelogin.log.session_start.player") + ": **" + player + "**\n" +
                                LangManager.get(lang, "hooks.management.librelogin.log.session_start.method") + ": **" + method + "**"
                );
            }

            // SessionEndEvent
            if (name.equals("com.github.librelogin.api.events.SessionEndEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                Object reason = event.getClass().getMethod("getReason").invoke(event);

                DiscordLog.send(
                        pluginKey,
                        LangManager.get(lang, "hooks.management.librelogin.log.session_end.title"),
                        LangManager.get(lang, "hooks.management.librelogin.log.session_end.player") + ": **" + player + "**\n" +
                                LangManager.get(lang, "hooks.management.librelogin.log.session_end.reason") + ": **" + reason + "**"
                );
            }

            // PasswordChangeEvent
            if (name.equals("com.github.librelogin.api.events.PasswordChangeEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);

                DiscordLog.send(
                        pluginKey,
                        LangManager.get(lang, "hooks.management.librelogin.log.password_change.title"),
                        LangManager.get(lang, "hooks.management.librelogin.log.password_change.player") + ": **" + player + "**"
                );
            }

            // TwoFactorEnableEvent
            if (name.equals("com.github.librelogin.api.events.TwoFactorEnableEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);

                DiscordLog.send(
                        pluginKey,
                        LangManager.get(lang, "hooks.management.librelogin.log.twofactor_enable.title"),
                        LangManager.get(lang, "hooks.management.librelogin.log.twofactor_enable.player") + ": **" + player + "**"
                );
            }

            // TwoFactorDisableEvent
            if (name.equals("com.github.librelogin.api.events.TwoFactorDisableEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);

                DiscordLog.send(
                        pluginKey,
                        LangManager.get(lang, "hooks.management.librelogin.log.twofactor_disable.title"),
                        LangManager.get(lang, "hooks.management.librelogin.log.twofactor_disable.player") + ": **" + player + "**"
                );
            }

        } catch (Exception ignored) {}
    }
}
