package org.respawn.omniConnect.hooks.anticheat;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class NCPHook implements Listener {

    private final String pluginKey;

    public NCPHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onCheat(Event event) {
        if (!event.getClass().getName().equals("fr.neatmonster.nocheatplus.event.CheatEvent")) return;

        try {
            Object player = event.getClass().getMethod("getPlayer").invoke(event);
            Object type = event.getClass().getMethod("getType").invoke(event);
            int vl = (int) event.getClass().getMethod("getLevel").invoke(event);
            String playerName = (String) player.getClass().getMethod("getName").invoke(player);

            DiscordLog.send(pluginKey,
                    "⚠️ NoCheatPlus riasztás",
                    "Játékos: **" + playerName + "**\nCheck: **" + type.toString() + "**\nVL: **" + vl + "**"
            );
        } catch (Exception ignored) {}
    }
}
