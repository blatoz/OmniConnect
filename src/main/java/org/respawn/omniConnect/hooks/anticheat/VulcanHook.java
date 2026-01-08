package org.respawn.omniConnect.hooks.anticheat;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class VulcanHook implements Listener {

    private final String pluginKey;

    public VulcanHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onFlag(Event event) {
        if (!event.getClass().getName().equals("me.frep.vulcan.api.event.VulcanFlagEvent")) return;

        try {
            Object player = event.getClass().getMethod("getPlayer").invoke(event);
            Object check = event.getClass().getMethod("getCheck").invoke(event);
            String playerName = (String) player.getClass().getMethod("getName").invoke(player);
            String checkName = (String) check.getClass().getMethod("getName").invoke(check);
            int vl = (int) check.getClass().getMethod("getVl").invoke(check);

            DiscordLog.send(pluginKey,
                    "⚠️ Vulcan Riasztás",
                    "Játékos: **" + playerName + "**\nCheck: **" + checkName + "**\nVL(Szabálysértés): **" + vl + "**"
            );
        } catch (Exception ignored) {}
    }
}
