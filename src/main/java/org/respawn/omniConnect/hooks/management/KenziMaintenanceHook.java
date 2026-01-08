package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class KenziMaintenanceHook implements Listener {

    private final String pluginKey;

    public KenziMaintenanceHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] Kenzi’s Maintenance hook aktiválva!");
    }

    @EventHandler
    public void onKenziMaintenanceEvent(Event event) {
        String name = event.getClass().getName();

        try {
            // ============================================================
            // MaintenanceEnableEvent
            // ============================================================
            if (name.equals("de.kenzoxx.Maintenance.api.events.MaintenanceEnableEvent")) {

                Object executorObj = event.getClass().getMethod("getExecutor").invoke(event);
                String executor = executorObj != null ? executorObj.toString() : "Ismeretlen";

                DiscordLog.send(
                        pluginKey,
                        "🛠️ Kenzi’s Maintenance Bekapcsolva",
                        "Végrehajtotta: **" + executor + "**"
                );
            }

            // ============================================================
            // MaintenanceDisableEvent
            // ============================================================
            if (name.equals("de.kenzoxx.Maintenance.api.events.MaintenanceDisableEvent")) {

                Object executorObj = event.getClass().getMethod("getExecutor").invoke(event);
                String executor = executorObj != null ? executorObj.toString() : "Ismeretlen";

                DiscordLog.send(
                        pluginKey,
                        "🟢 Kenzi’s Maintenance Kikapcsolva",
                        "Végrehajtotta: **" + executor + "**"
                );
            }

        } catch (Exception ignored) {}
    }
}
