package org.respawn.omniConnect.hooks.economy;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class CoinManagerHook implements Listener {

    private final String pluginKey;

    public CoinManagerHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onCoinEvent(Event event) {
        String name = event.getClass().getName();

        try {
            switch (name) {

                case "me.realized.coinmanager.api.events.CoinAddEvent": {
                    Object player = event.getClass().getMethod("getPlayer").invoke(event);
                    int amount = (int) event.getClass().getMethod("getAmount").invoke(event);
                    int balance = (int) event.getClass().getMethod("getBalance").invoke(event);

                    String playerName = (String) player.getClass().getMethod("getName").invoke(player);

                    DiscordLog.send(pluginKey,
                            "🪙 CoinManager – Coin Hozzáadva",
                            "Játékos: **" + playerName + "**\n"
                                    + "Hozzáadott: **" + amount + "**\n"
                                    + "Új Egyenleg: **" + balance + "**"
                    );
                    break;
                }

                case "me.realized.coinmanager.api.events.CoinRemoveEvent": {
                    Object player = event.getClass().getMethod("getPlayer").invoke(event);
                    int amount = (int) event.getClass().getMethod("getAmount").invoke(event);
                    int balance = (int) event.getClass().getMethod("getBalance").invoke(event);

                    String playerName = (String) player.getClass().getMethod("getName").invoke(player);

                    DiscordLog.send(pluginKey,
                            "💸 CoinManager – Coin Levonva",
                            "Játékos: **" + playerName + "**\n"
                                    + "Levonva: **" + amount + "**\n"
                                    + "Új Egyenleg: **" + balance + "**"
                    );
                    break;
                }

                case "me.realized.coinmanager.api.events.CoinSetEvent": {
                    Object player = event.getClass().getMethod("getPlayer").invoke(event);
                    int balance = (int) event.getClass().getMethod("getBalance").invoke(event);

                    String playerName = (String) player.getClass().getMethod("getName").invoke(player);

                    DiscordLog.send(pluginKey,
                            "⚙️ CoinManager – Coin Beállítva",
                            "Játékos: **" + playerName + "**\n"
                                    + "Új Egyenleg: **" + balance + "**"
                    );
                    break;
                }

                default:
                    break;
            }

        } catch (Exception ignored) {}
    }
}
