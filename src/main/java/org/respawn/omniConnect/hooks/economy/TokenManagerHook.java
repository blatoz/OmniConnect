package org.respawn.omniConnect.hooks.economy;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class TokenManagerHook implements Listener {

    private final String pluginKey;

    public TokenManagerHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] TokenManager hook has been enabled!");
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onTokenManagerEvent(Event event) {
        String name = event.getClass().getName();

        try {
            String lang = lang();

            // TokenAddEvent
            if (name.equals("me.realized.tokenmanager.api.events.TokenAddEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                Object amount = event.getClass().getMethod("getAmount").invoke(event);
                Object executor = event.getClass().getMethod("getExecutor").invoke(event);

                String title = LangManager.get(lang, "hooks.economy.tokenmanager.log.add.title");
                String body =
                        LangManager.get(lang, "hooks.economy.tokenmanager.log.add.player") + ": **" + player + "**\n" +
                                LangManager.get(lang, "hooks.economy.tokenmanager.log.add.amount") + ": **" + amount + "**\n" +
                                LangManager.get(lang, "hooks.economy.tokenmanager.log.add.executor") + ": **" + executor + "**";

                DiscordLog.send(pluginKey, title, body);
            }

            // TokenRemoveEvent
            if (name.equals("me.realized.tokenmanager.api.events.TokenRemoveEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                Object amount = event.getClass().getMethod("getAmount").invoke(event);
                Object executor = event.getClass().getMethod("getExecutor").invoke(event);

                String title = LangManager.get(lang, "hooks.economy.tokenmanager.log.remove.title");
                String body =
                        LangManager.get(lang, "hooks.economy.tokenmanager.log.remove.player") + ": **" + player + "**\n" +
                                LangManager.get(lang, "hooks.economy.tokenmanager.log.remove.amount") + ": **" + amount + "**\n" +
                                LangManager.get(lang, "hooks.economy.tokenmanager.log.remove.executor") + ": **" + executor + "**";

                DiscordLog.send(pluginKey, title, body);
            }

            // TokenSetEvent
            if (name.equals("me.realized.tokenmanager.api.events.TokenSetEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                Object amount = event.getClass().getMethod("getAmount").invoke(event);
                Object executor = event.getClass().getMethod("getExecutor").invoke(event);

                String title = LangManager.get(lang, "hooks.economy.tokenmanager.log.set.title");
                String body =
                        LangManager.get(lang, "hooks.economy.tokenmanager.log.set.player") + ": **" + player + "**\n" +
                                LangManager.get(lang, "hooks.economy.tokenmanager.log.set.amount") + ": **" + amount + "**\n" +
                                LangManager.get(lang, "hooks.economy.tokenmanager.log.set.executor") + ": **" + executor + "**";

                DiscordLog.send(pluginKey, title, body);
            }

            // TokenResetEvent
            if (name.equals("me.realized.tokenmanager.api.events.TokenResetEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                Object executor = event.getClass().getMethod("getExecutor").invoke(event);

                String title = LangManager.get(lang, "hooks.economy.tokenmanager.log.reset.title");
                String body =
                        LangManager.get(lang, "hooks.economy.tokenmanager.log.reset.player") + ": **" + player + "**\n" +
                                LangManager.get(lang, "hooks.economy.tokenmanager.log.reset.executor") + ": **" + executor + "**";

                DiscordLog.send(pluginKey, title, body);
            }

            // TokenPayEvent
            if (name.equals("me.realized.tokenmanager.api.events.TokenPayEvent")) {

                Object from = event.getClass().getMethod("getSender").invoke(event);
                Object to = event.getClass().getMethod("getReceiver").invoke(event);
                Object amount = event.getClass().getMethod("getAmount").invoke(event);

                String title = LangManager.get(lang, "hooks.economy.tokenmanager.log.pay.title");
                String body =
                        LangManager.get(lang, "hooks.economy.tokenmanager.log.pay.from") + ": **" + from + "**\n" +
                                LangManager.get(lang, "hooks.economy.tokenmanager.log.pay.to") + ": **" + to + "**\n" +
                                LangManager.get(lang, "hooks.economy.tokenmanager.log.pay.amount") + ": **" + amount + "**";

                DiscordLog.send(pluginKey, title, body);
            }

        } catch (Exception ignored) {}
    }
}
