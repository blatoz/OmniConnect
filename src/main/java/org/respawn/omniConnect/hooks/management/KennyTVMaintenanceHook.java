package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class KennyTVMaintenanceHook implements Listener {

    private final String pluginKey;

    public KennyTVMaintenanceHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] KennyTV Maintenance hook has been enabled!");
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onMaintenanceEvent(Event event) {
        String name = event.getClass().getName().toLowerCase();

        try {
            String lang = lang();

            // Enable
            if (name.contains("maintenanceenableevent")) {

                String title = LangManager.get(lang, "hooks.management.kennytvmaintenance.log.enable.title");
                String desc = LangManager.get(lang, "hooks.management.kennytvmaintenance.log.enable.description");

                DiscordLog.send(pluginKey, title, desc);
            }

            // Disable
            if (name.contains("maintenancedisableevent")) {

                String title = LangManager.get(lang, "hooks.management.kennytvmaintenance.log.disable.title");
                String desc = LangManager.get(lang, "hooks.management.kennytvmaintenance.log.disable.description");

                DiscordLog.send(pluginKey, title, desc);
            }

            // Whitelist Add
            if (name.contains("whitelistaddevent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                String playerName = player != null ? player.toString() : "Unknown";

                String title = LangManager.get(lang, "hooks.management.kennytvmaintenance.log.whitelist_add.title");
                String body =
                        LangManager.get(lang, "hooks.management.kennytvmaintenance.log.whitelist_add.player")
                                + ": **" + playerName + "**";

                DiscordLog.send(pluginKey, title, body);
            }

            // Whitelist Remove
            if (name.contains("whitelistremoveevent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                String playerName = player != null ? player.toString() : "Unknown";

                String title = LangManager.get(lang, "hooks.management.kennytvmaintenance.log.whitelist_remove.title");
                String body =
                        LangManager.get(lang, "hooks.management.kennytvmaintenance.log.whitelist_remove.player")
                                + ": **" + playerName + "**";

                DiscordLog.send(pluginKey, title, body);
            }

            // Kick
            if (name.contains("kickevent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                String playerName = player != null ? player.toString() : "Unknown";

                String title = LangManager.get(lang, "hooks.management.kennytvmaintenance.log.kick.title");
                String body =
                        LangManager.get(lang, "hooks.management.kennytvmaintenance.log.kick.player")
                                + ": **" + playerName + "**\n" +
                                LangManager.get(lang, "hooks.management.kennytvmaintenance.log.kick.reason")
                                + ": **Maintenance mode**";

                DiscordLog.send(pluginKey, title, body);
            }

        } catch (Exception ignored) {}
    }
}
