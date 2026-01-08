package org.respawn.omniConnect.hooks.anticheat;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class NegativityHook implements Listener {

    private final String pluginKey;

    public NegativityHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onCheat(Event event) {
        if (!event.getClass().getName().equals("com.elikill58.negativity.api.events.PlayerCheatEvent")) return;

        try {
            Object player = event.getClass().getMethod("getPlayer").invoke(event);
            Object cheat = event.getClass().getMethod("getCheat").invoke(event);
            int reliability = (int) event.getClass().getMethod("getReliability").invoke(event);
            String playerName = (String) player.getClass().getMethod("getName").invoke(player);
            String cheatName = (String) cheat.getClass().getMethod("getName").invoke(cheat);

            DiscordLog.send(pluginKey,
                    "⚠️ Negativity Riasztás",
                    "Játékos: **" + playerName + "**\nCsalás Típus: **" + cheatName + "**\nReliability: **" + reliability + "**"
            );
        } catch (Exception ignored) {}
    }
}

