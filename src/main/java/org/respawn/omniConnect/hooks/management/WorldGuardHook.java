package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class WorldGuardHook implements Listener {

    private final String pluginKey;

    public WorldGuardHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] WorldGuard hook aktiválva!");
    }

    @EventHandler
    public void onWorldGuardEvent(Event event) {
        String name = event.getClass().getName();

        try {

            // ============================================================
            // Régió létrehozása
            // com.sk89q.worldguard.bukkit.event.region.RegionCreateEvent
            // ============================================================
            if (name.equals("com.sk89q.worldguard.bukkit.event.region.RegionCreateEvent")) {

                Object region = event.getClass().getMethod("getRegion").invoke(event);
                String id = (String) region.getClass().getMethod("getId").invoke(region);

                Object actor = event.getClass().getMethod("getActor").invoke(event);
                String executor = actor != null ? actor.toString() : "Ismeretlen";

                DiscordLog.send(
                        pluginKey,
                        "📐 WorldGuard – Régió Létrehozva",
                        "Régió: **" + id + "**\n"
                                + "Létrehozta: **" + executor + "**"
                );
            }

            // ============================================================
            // Régió törlése
            // com.sk89q.worldguard.bukkit.event.region.RegionDeleteEvent
            // ============================================================
            if (name.equals("com.sk89q.worldguard.bukkit.event.region.RegionDeleteEvent")) {

                String id = (String) event.getClass().getMethod("getRegionId").invoke(event);

                Object actor = event.getClass().getMethod("getActor").invoke(event);
                String executor = actor != null ? actor.toString() : "Ismeretlen";

                DiscordLog.send(
                        pluginKey,
                        "🗑️ WorldGuard – Régió Törölve",
                        "Régió: **" + id + "**\n"
                                + "Törölte: **" + executor + "**"
                );
            }

            // ============================================================
            // Régió módosítása (owners, members, flags, priority, boundaries)
            // com.sk89q.worldguard.bukkit.event.region.RegionUpdateEvent
            // ============================================================
            if (name.equals("com.sk89q.worldguard.bukkit.event.region.RegionUpdateEvent")) {

                Object region = event.getClass().getMethod("getRegion").invoke(event);
                String id = (String) region.getClass().getMethod("getId").invoke(region);

                Object actor = event.getClass().getMethod("getActor").invoke(event);
                String executor = actor != null ? actor.toString() : "Ismeretlen";

                DiscordLog.send(
                        pluginKey,
                        "✏️ WorldGuard – Régió Módosítva",
                        "Régió: **" + id + "**\n"
                                + "Módosította: **" + executor + "**"
                );
            }

            // ============================================================
            // Flag érték változás
            // com.sk89q.worldguard.bukkit.event.flag.FlagValueChangeEvent
            // ============================================================
            if (name.equals("com.sk89q.worldguard.bukkit.event.flag.FlagValueChangeEvent")) {

                Object flag = event.getClass().getMethod("getFlag").invoke(event);
                Object oldVal = event.getClass().getMethod("getOldValue").invoke(event);
                Object newVal = event.getClass().getMethod("getNewValue").invoke(event);

                Object region = event.getClass().getMethod("getRegion").invoke(event);
                String id = (String) region.getClass().getMethod("getId").invoke(region);

                Object actor = event.getClass().getMethod("getActor").invoke(event);
                String executor = actor != null ? actor.toString() : "Ismeretlen";

                DiscordLog.send(
                        pluginKey,
                        "🚩 WorldGuard – Flag Módosítva",
                        "Régió: **" + id + "**\n"
                                + "Flag: **" + flag + "**\n"
                                + "Régi Érték: **" + oldVal + "**\n"
                                + "Új Érték: **" + newVal + "**\n"
                                + "Módosította: **" + executor + "**"
                );
            }

        } catch (Exception ignored) {}
    }
}
