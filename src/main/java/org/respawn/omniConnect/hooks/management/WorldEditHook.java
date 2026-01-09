package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class WorldEditHook implements Listener {

    private final String pluginKey;

    public WorldEditHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] WorldEdit hook aktiválva!");
    }

    // ------------------------------------------------------------
    // WorldEdit parancsok figyelése (//set, //replace, //undo, stb.)
    // ------------------------------------------------------------
    @EventHandler
    public void onWorldEditCommand(PlayerCommandPreprocessEvent event) {
        String msg = event.getMessage();
        if (!isWorldEditCommand(msg)) {
            return;
        }

        String playerName = event.getPlayer().getName();

        DiscordLog.send(
                pluginKey,
                "🧱 WorldEdit – Parancs Végrehajtva",
                "Játékos: **" + playerName + "**\n"
                        + "Parancs: `" + msg + "`"
        );
    }

    private boolean isWorldEditCommand(String raw) {
        if (raw == null) return false;

        String cmd = raw.trim().toLowerCase();

        // Klasszikus WorldEdit parancsok (rövid és hosszú formák)
        return cmd.startsWith("//")
                || cmd.startsWith("/worldedit")
                || cmd.startsWith("/we ")
                || cmd.equals("/we");
    }

    // ------------------------------------------------------------
    // WorldEdit EditSession esemény figyelése reflectionnel
    // com.sk89q.worldedit.event.extent.EditSessionEvent
    // ------------------------------------------------------------
    @EventHandler
    public void onWorldEditEvent(Event event) {
        String name = event.getClass().getName();

        try {
            if (name.equals("com.sk89q.worldedit.event.extent.EditSessionEvent")) {

                Object actor = event.getClass().getMethod("getActor").invoke(event);
                String executor = actor != null ? actor.toString() : "Ismeretlen";

                Object world = event.getClass().getMethod("getWorld").invoke(event);
                String worldName = world != null ? world.toString() : "Ismeretlen";

                DiscordLog.send(
                        pluginKey,
                        "🧱 WorldEdit – EditSession Létrehozva",
                        "Végrehajtó: **" + executor + "**\n"
                                + "Világ: **" + worldName + "**"
                );
            }

        } catch (Exception ignored) {}
    }
}
