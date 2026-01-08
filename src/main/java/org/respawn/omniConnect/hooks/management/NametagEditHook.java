package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class NametagEditHook implements Listener {

    private final String pluginKey;

    public NametagEditHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] NametagEdit hook aktiválva!");
    }

    @EventHandler
    public void onNametagEditEvent(Event event) {
        String name = event.getClass().getName();

        try {
            // ============================================================
            // NametagEdit - PlayerTagChangeEvent
            // com.nametagedit.plugin.api.events.PlayerTagChangeEvent
            // ============================================================
            if (name.equals("com.nametagedit.plugin.api.events.PlayerTagChangeEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                String playerName = player != null ? player.toString() : "Ismeretlen";

                String oldPrefix = (String) event.getClass().getMethod("getOldPrefix").invoke(event);
                String newPrefix = (String) event.getClass().getMethod("getNewPrefix").invoke(event);

                String oldSuffix = (String) event.getClass().getMethod("getOldSuffix").invoke(event);
                String newSuffix = (String) event.getClass().getMethod("getNewSuffix").invoke(event);

                DiscordLog.send(
                        pluginKey,
                        "🏷️ NametagEdit – Prefix/Suffix módosítva",
                        "Játékos: **" + playerName + "**\n"
                                + "Régi prefix: `" + oldPrefix + "`\n"
                                + "Új prefix: `" + newPrefix + "`\n"
                                + "Régi suffix: `" + oldSuffix + "`\n"
                                + "Új suffix: `" + newSuffix + "`"
                );
            }

        } catch (Exception ignored) {}
    }
}
