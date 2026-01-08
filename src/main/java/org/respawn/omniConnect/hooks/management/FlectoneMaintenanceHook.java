package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class FlectoneMaintenanceHook implements Listener {

    private final String pluginKey;

    public FlectoneMaintenanceHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] Flectone Maintenance hook aktiválva!");
    }

    @EventHandler
    public void onFlectoneMaintenanceEvent(Event event) {
        String name = event.getClass().getName();

        try {
            // ============================================================
            // MaintenanceModeEnableEvent
            // ============================================================
            if (name.equals("ru.flectone.maintenance.api.events.MaintenanceModeEnableEvent")) {

                Object executorObj = event.getClass().getMethod("getExecutor").invoke(event);
                String executor = executorObj != null ? executorObj.toString() : "Ismeretlen";

                DiscordLog.send(
                        pluginKey,
                        "🛠️ Flectone Maintenance bekapcsolva",
                        "Végrehajtotta: **" + executor + "**"
                );
            }

            // ============================================================
            // MaintenanceModeDisableEvent
            // ============================================================
            if (name.equals("ru.flectone.maintenance.api.events.MaintenanceModeDisableEvent")) {

                Object executorObj = event.getClass().getMethod("getExecutor").invoke(event);
                String executor = executorObj != null ? executorObj.toString() : "Ismeretlen";

                DiscordLog.send(
                        pluginKey,
                        "🟢 Flectone Maintenance kikapcsolva",
                        "Végrehajtotta: **" + executor + "**"
                );
            }

        } catch (Exception ignored) {}
    }
}
