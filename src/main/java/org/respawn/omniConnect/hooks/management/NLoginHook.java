package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class NLoginHook implements Listener {

    private final String pluginKey;

    public NLoginHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] nLogin hook has been enabled!");
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onNLoginEvent(Event event) {
        String name = event.getClass().getName();

        try {
            String lang = lang();

            // PlayerLoginEvent
            if (name.equals("com.nickuc.login.api.events.PlayerLoginEvent")) {
                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                DiscordLog.send(pluginKey,
                        LangManager.get(lang, "hooks.management.nlogin.log.login.title"),
                        LangManager.get(lang, "hooks.management.nlogin.log.login.player") + ": **" + player + "**"
                );
            }

            // PlayerRegisterEvent
            if (name.equals("com.nickuc.login.api.events.PlayerRegisterEvent")) {
                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                DiscordLog.send(pluginKey,
                        LangManager.get(lang, "hooks.management.nlogin.log.register.title"),
                        LangManager.get(lang, "hooks.management.nlogin.log.register.player") + ": **" + player + "**"
                );
            }

            // PlayerLogoutEvent
            if (name.equals("com.nickuc.login.api.events.PlayerLogoutEvent")) {
                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                DiscordLog.send(pluginKey,
                        LangManager.get(lang, "hooks.management.nlogin.log.logout.title"),
                        LangManager.get(lang, "hooks.management.nlogin.log.logout.player") + ": **" + player + "**"
                );
            }

            // PlayerPasswordChangeEvent
            if (name.equals("com.nickuc.login.api.events.PlayerPasswordChangeEvent")) {
                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                DiscordLog.send(pluginKey,
                        LangManager.get(lang, "hooks.management.nlogin.log.password_change.title"),
                        LangManager.get(lang, "hooks.management.nlogin.log.password_change.player") + ": **" + player + "**"
                );
            }

        } catch (Exception ignored) {}
    }
}
