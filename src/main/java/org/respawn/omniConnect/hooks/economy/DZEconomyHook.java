package org.respawn.omniConnect.hooks.economy;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class DZEconomyHook implements Listener {

    private final String pluginKey;

    public DZEconomyHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onDZEco(Event event) {
        if (!event.getClass().getName().equals("me.dzeconomy.api.events.BalanceUpdateEvent"))
            return;

        try {
            Object player = event.getClass().getMethod("getPlayer").invoke(event);
            double oldBal = (double) event.getClass().getMethod("getOldBalance").invoke(event);
            double newBal = (double) event.getClass().getMethod("getNewBalance").invoke(event);

            String name = (String) player.getClass().getMethod("getName").invoke(player);
            double diff = newBal - oldBal;

            DiscordLog.send(pluginKey,
                    "💵 DZEconomy Változás",
                    "Játékos: **" + name + "**\n"
                            + "Változás: **" + diff + "**\n"
                            + "Új Egyenleg: **" + newBal + "**"
            );

        } catch (Exception ignored) {}
    }
}
