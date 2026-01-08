package org.respawn.omniConnect.hooks.economy;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class PlayerPointsHook implements Listener {

    private final String pluginKey;

    public PlayerPointsHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onPointsEvent(Event event) {
        String name = event.getClass().getName();

        try {
            switch (name) {

                case "org.black_ixx.playerpoints.event.PlayerPointsChangeEvent": {
                    Object player = event.getClass().getMethod("getPlayer").invoke(event);
                    int amount = (int) event.getClass().getMethod("getChange").invoke(event);
                    int newBalance = (int) event.getClass().getMethod("getNewBalance").invoke(event);

                    String playerName = (String) player.getClass().getMethod("getName").invoke(player);

                    DiscordLog.send(pluginKey,
                            "💠 PlayerPoints Változás",
                            "Játékos: **" + playerName + "**\n"
                                    + "Változás: **" + amount + "**\n"
                                    + "Új Egyenleg: **" + newBalance + "**"
                    );
                    break;
                }

                case "org.black_ixx.playerpoints.event.PlayerPointsResetEvent": {
                    Object player = event.getClass().getMethod("getPlayer").invoke(event);
                    String playerName = (String) player.getClass().getMethod("getName").invoke(player);

                    DiscordLog.send(pluginKey,
                            "♻️ PlayerPoints Visszaállítás",
                            "Játékos: **" + playerName + "** pontjai nullázva lettek."
                    );
                    break;
                }

                default:
                    break;
            }

        } catch (Exception ignored) {}
    }
}
