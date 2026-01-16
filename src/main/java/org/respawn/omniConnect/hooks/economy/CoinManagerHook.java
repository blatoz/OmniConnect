package org.respawn.omniConnect.hooks.economy;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class CoinManagerHook implements Listener {

    private final String pluginKey;

    public CoinManagerHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onCoinEvent(Event event) {
        String name = event.getClass().getName();

        try {
            String lang = lang();

            switch (name) {

                case "me.realized.coinmanager.api.events.CoinAddEvent": {
                    Object player = event.getClass().getMethod("getPlayer").invoke(event);
                    int amount = (int) event.getClass().getMethod("getAmount").invoke(event);
                    int balance = (int) event.getClass().getMethod("getBalance").invoke(event);

                    String playerName = (String) player.getClass().getMethod("getName").invoke(player);

                    String title = LangManager.get(lang, "hooks.economy.coinmanager.log.add.title");

                    String body =
                            LangManager.get(lang, "hooks.economy.coinmanager.log.add.player") + ": **" + playerName + "**\n" +
                                    LangManager.get(lang, "hooks.economy.coinmanager.log.add.amount") + ": **" + amount + "**\n" +
                                    LangManager.get(lang, "hooks.economy.coinmanager.log.add.balance") + ": **" + balance + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                case "me.realized.coinmanager.api.events.CoinRemoveEvent": {
                    Object player = event.getClass().getMethod("getPlayer").invoke(event);
                    int amount = (int) event.getClass().getMethod("getAmount").invoke(event);
                    int balance = (int) event.getClass().getMethod("getBalance").invoke(event);

                    String playerName = (String) player.getClass().getMethod("getName").invoke(player);

                    String title = LangManager.get(lang, "hooks.economy.coinmanager.log.remove.title");

                    String body =
                            LangManager.get(lang, "hooks.economy.coinmanager.log.remove.player") + ": **" + playerName + "**\n" +
                                    LangManager.get(lang, "hooks.economy.coinmanager.log.remove.amount") + ": **" + amount + "**\n" +
                                    LangManager.get(lang, "hooks.economy.coinmanager.log.remove.balance") + ": **" + balance + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                case "me.realized.coinmanager.api.events.CoinSetEvent": {
                    Object player = event.getClass().getMethod("getPlayer").invoke(event);
                    int balance = (int) event.getClass().getMethod("getBalance").invoke(event);

                    String playerName = (String) player.getClass().getMethod("getName").invoke(player);

                    String title = LangManager.get(lang, "hooks.economy.coinmanager.log.set.title");

                    String body =
                            LangManager.get(lang, "hooks.economy.coinmanager.log.set.player") + ": **" + playerName + "**\n" +
                                    LangManager.get(lang, "hooks.economy.coinmanager.log.set.balance") + ": **" + balance + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                default:
                    break;
            }

        } catch (Exception ignored) {}
    }
}
