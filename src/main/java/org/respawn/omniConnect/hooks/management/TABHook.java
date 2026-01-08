package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class TABHook implements Listener {

    private final String pluginKey;

    public TABHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] TAB hook aktiválva!");
    }

    @EventHandler
    public void onTabEvent(Event event) {
        String name = event.getClass().getName();

        try {
            // ============================================================
            // TAB prefix/suffix változás
            // me.neznamy.tab.api.event.player.PlayerLoadEvent
            // me.neznamy.tab.api.event.player.PlayerDisplayNameChangeEvent
            // me.neznamy.tab.api.event.player.PlayerPrefixSuffixChangeEvent
            // ============================================================

            if (name.equals("me.neznamy.tab.api.event.player.PlayerPrefixSuffixChangeEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                String playerName = player != null ? player.toString() : "Ismeretlen";

                String oldPrefix = (String) event.getClass().getMethod("getOldPrefix").invoke(event);
                String newPrefix = (String) event.getClass().getMethod("getNewPrefix").invoke(event);

                String oldSuffix = (String) event.getClass().getMethod("getOldSuffix").invoke(event);
                String newSuffix = (String) event.getClass().getMethod("getNewSuffix").invoke(event);

                DiscordLog.send(
                        pluginKey,
                        "🏷️ TAB – Prefix/Suffix Módosítva",
                        "Játékos: **" + playerName + "**\n"
                                + "Régi Prefix: `" + oldPrefix + "`\n"
                                + "Új Prefix: `" + newPrefix + "`\n"
                                + "Régi Suffix: `" + oldSuffix + "`\n"
                                + "Új Suffix: `" + newSuffix + "`"
                );
            }

        } catch (Exception ignored) {}
    }
}
