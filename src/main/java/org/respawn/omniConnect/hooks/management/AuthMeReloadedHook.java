package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class AuthMeReloadedHook implements Listener {

    private final String pluginKey;

    public AuthMeReloadedHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] AuthMe Reloaded hook has been enabled!");
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onAuthMeReloadedEvent(Event event) {
        String name = event.getClass().getName();

        try {
            String lang = lang();

            // LoginEvent
            if (name.equals("fr.xephi.authme.events.LoginEvent")) {
                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                DiscordLog.send(pluginKey,
                        LangManager.get(lang, "hooks.management.authmere.loaded.log.login.title"),
                        LangManager.get(lang, "hooks.management.authmere.loaded.log.login.player") + ": **" + player + "**"
                );
            }

            // RegisterEvent
            if (name.equals("fr.xephi.authme.events.RegisterEvent")) {
                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                DiscordLog.send(pluginKey,
                        LangManager.get(lang, "hooks.management.authmere.loaded.log.register.title"),
                        LangManager.get(lang, "hooks.management.authmere.loaded.log.register.player") + ": **" + player + "**"
                );
            }

            // LogoutEvent
            if (name.equals("fr.xephi.authme.events.LogoutEvent")) {
                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                DiscordLog.send(pluginKey,
                        LangManager.get(lang, "hooks.management.authmere.loaded.log.logout.title"),
                        LangManager.get(lang, "hooks.management.authmere.loaded.log.logout.player") + ": **" + player + "**"
                );
            }

            // PasswordChangedEvent
            if (name.equals("fr.xephi.authme.events.PasswordChangedEvent")) {
                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                DiscordLog.send(pluginKey,
                        LangManager.get(lang, "hooks.management.authmere.loaded.log.password_change.title"),
                        LangManager.get(lang, "hooks.management.authmere.loaded.log.password_change.player") + ": **" + player + "**"
                );
            }

        } catch (Exception ignored) {}
    }
}
