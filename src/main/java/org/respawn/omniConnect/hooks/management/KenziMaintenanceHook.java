package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class KenziMaintenanceHook implements Listener {

    private final String pluginKey;

    public KenziMaintenanceHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] Kenzi’s Maintenance hook has been enabled!");
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onKenziMaintenanceEvent(Event event) {
        String name = event.getClass().getName();

        try {
            String lang = lang();

            // Enable
            if (name.equals("de.kenzoxx.Maintenance.api.events.MaintenanceEnableEvent")) {

                Object executorObj = event.getClass().getMethod("getExecutor").invoke(event);
                String executor = executorObj != null ? executorObj.toString() : "Ismeretlen";

                String title = LangManager.get(lang, "hooks.management.kenzimaintenance.log.enable.title");
                String body =
                        LangManager.get(lang, "hooks.management.kenzimaintenance.log.enable.executor")
                                + ": **" + executor + "**";

                DiscordLog.send(pluginKey, title, body);
            }

            // Disable
            if (name.equals("de.kenzoxx.Maintenance.api.events.MaintenanceDisableEvent")) {

                Object executorObj = event.getClass().getMethod("getExecutor").invoke(event);
                String executor = executorObj != null ? executorObj.toString() : "Ismeretlen";

                String title = LangManager.get(lang, "hooks.management.kenzimaintenance.log.disable.title");
                String body =
                        LangManager.get(lang, "hooks.management.kenzimaintenance.log.disable.executor")
                                + ": **" + executor + "**";

                DiscordLog.send(pluginKey, title, body);
            }

        } catch (Exception ignored) {}
    }
}
