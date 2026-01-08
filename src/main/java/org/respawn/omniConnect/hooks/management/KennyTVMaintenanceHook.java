package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class KennyTVMaintenanceHook implements Listener {

    private final String pluginKey;

    public KennyTVMaintenanceHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] KennyTV Maintenance hook aktiválva!");
    }

    @EventHandler
    public void onMaintenanceEvent(Event event) {
        String name = event.getClass().getName().toLowerCase();

        try {

            // ============================================================
            // Maintenance mód bekapcsolva
            // eu.kennytv.maintenance.api.event.MaintenanceEnableEvent
            // ============================================================
            if (name.contains("maintenanceenableevent")) {

                DiscordLog.send(
                        pluginKey,
                        "🛠️ KennyTV Maintenance – Bekapcsolva",
                        "A szerver karbantartási módba lépett."
                );
            }

            // ============================================================
            // Maintenance mód kikapcsolva
            // eu.kennytv.maintenance.api.event.MaintenanceDisableEvent
            // ============================================================
            if (name.contains("maintenancedisableevent")) {

                DiscordLog.send(
                        pluginKey,
                        "🛠️ KennyTV Maintenance – Kikapcsolva",
                        "A szerver kilépett a karbantartási módból."
                );
            }

            // ============================================================
            // Whitelist hozzáadás
            // eu.kennytv.maintenance.api.event.MaintenanceWhitelistAddEvent
            // ============================================================
            if (name.contains("whitelistaddevent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                String playerName = player != null ? player.toString() : "Ismeretlen";

                DiscordLog.send(
                        pluginKey,
                        "🛠️ KennyTV Maintenance – Whitelist hozzáadva",
                        "Játékos: **" + playerName + "**"
                );
            }

            // ============================================================
            // Whitelist eltávolítás
            // eu.kennytv.maintenance.api.event.MaintenanceWhitelistRemoveEvent
            // ============================================================
            if (name.contains("whitelistremoveevent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                String playerName = player != null ? player.toString() : "Ismeretlen";

                DiscordLog.send(
                        pluginKey,
                        "🛠️ KennyTV Maintenance – Whitelist eltávolítva",
                        "Játékos: **" + playerName + "**"
                );
            }

            // ============================================================
            // Kick event (maintenance miatt)
            // eu.kennytv.maintenance.api.event.MaintenanceKickEvent
            // ============================================================
            if (name.contains("kickevent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                String playerName = player != null ? player.toString() : "Ismeretlen";

                DiscordLog.send(
                        pluginKey,
                        "🛠️ KennyTV Maintenance – Játékos kirúgva",
                        "Játékos: **" + playerName + "**\n"
                                + "Indok: Karbantartási mód"
                );
            }

        } catch (Exception ignored) {}
    }
}
