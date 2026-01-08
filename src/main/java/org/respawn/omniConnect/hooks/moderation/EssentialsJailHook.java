package org.respawn.omniConnect.hooks.moderation;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class EssentialsJailHook implements Listener {

    private final String pluginKey;

    public EssentialsJailHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onEssentialsJail(Event event) {
        String name = event.getClass().getName();

        try {
            switch (name) {

                case "com.earth2me.essentials.events.EssentialsJailEvent": {
                    Object player = event.getClass().getMethod("getAffected").invoke(event);
                    String target = (String) player.getClass().getMethod("getName").invoke(player);

                    DiscordLog.send(pluginKey, "🚨 EssentialsX Bebörtönzés", "Játékos: **" + target + "**");
                    break;
                }

                case "com.earth2me.essentials.events.EssentialsUnjailEvent": {
                    Object player = event.getClass().getMethod("getAffected").invoke(event);
                    String target = (String) player.getClass().getMethod("getName").invoke(player);

                    DiscordLog.send(pluginKey, "🔓 EssentialsX Bebörtönzés Törölve", "Játékos: **" + target + "**");
                    break;
                }

                default:
                    break;
            }

        } catch (Exception ignored) {}
    }
}
