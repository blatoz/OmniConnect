package org.respawn.omniConnect.hooks.economy;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class EssentialsEconomyHook implements Listener {

    private final String pluginKey;

    public EssentialsEconomyHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onEssentialsEco(Event event) {
        if (!event.getClass().getName().equals("com.earth2me.essentials.events.UserBalanceUpdateEvent"))
            return;

        try {
            Object user = event.getClass().getMethod("getUser").invoke(event);
            double oldBal = (double) event.getClass().getMethod("getOldBalance").invoke(event);
            double newBal = (double) event.getClass().getMethod("getNewBalance").invoke(event);

            String name = (String) user.getClass().getMethod("getName").invoke(user);
            double diff = newBal - oldBal;

            DiscordLog.send(pluginKey,
                    "💵 EssentialsX Economy Változás",
                    "Játékos: **" + name + "**\n"
                            + "Változás: **" + diff + "**\n"
                            + "Új Egyenleg: **" + newBal + "**"
            );

        } catch (Exception ignored) {}
    }
}
