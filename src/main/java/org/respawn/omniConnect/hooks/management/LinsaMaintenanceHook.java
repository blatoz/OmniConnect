package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class LinsaMaintenanceHook implements Listener {

    private final String pluginKey;

    public LinsaMaintenanceHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] LinsaFTW Maintenance hook aktiválva!");
    }

    @EventHandler
    public void onLinsaMaintenanceEvent(Event event) {
        String name = event.getClass().getName();

        try {
            // com.linsftw.maintenance.api.events.MaintenanceToggleEvent
            if (name.equals("com.linsftw.maintenance.api.events.MaintenanceToggleEvent")) {

                boolean enabled = (boolean) event.getClass().getMethod("isEnabled").invoke(event);
                Object executorObj = event.getClass().getMethod("getExecutor").invoke(event);
                String executor = executorObj != null ? executorObj.toString() : "Ismeretlen";

                DiscordLog.send(
                        pluginKey,
                        enabled ? "🛠️ LinsaFTW Maintenance bekapcsolva" : "🟢 LinsaFTW Maintenance kikapcsolva",
                        "Végrehajtotta: **" + executor + "**"
                );
            }

        } catch (Exception ignored) {}
    }
}
