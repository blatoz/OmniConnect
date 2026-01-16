package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class FlectoneMaintenanceHook implements Listener {

    private final String pluginKey;

    public FlectoneMaintenanceHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] Flectone Maintenance hook has been enabled!");
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onFlectoneMaintenanceEvent(Event event) {
        String name = event.getClass().getName();

        try {
            String lang = lang();

            // MaintenanceModeEnableEvent
            if (name.equals("ru.flectone.maintenance.api.events.MaintenanceModeEnableEvent")) {

                Object executorObj = event.getClass().getMethod("getExecutor").invoke(event);
                String executor = executorObj != null ? executorObj.toString() : "Ismeretlen";

                String title = LangManager.get(lang, "hooks.management.flectonemaintenance.log.enable.title");
                String body =
                        LangManager.get(lang, "hooks.management.flectonemaintenance.log.enable.executor")
                                + ": **" + executor + "**";

                DiscordLog.send(pluginKey, title, body);
            }

            // MaintenanceModeDisableEvent
            if (name.equals("ru.flectone.maintenance.api.events.MaintenanceModeDisableEvent")) {

                Object executorObj = event.getClass().getMethod("getExecutor").invoke(event);
                String executor = executorObj != null ? executorObj.toString() : "Ismeretlen";

                String title = LangManager.get(lang, "hooks.management.flectonemaintenance.log.disable.title");
                String body =
                        LangManager.get(lang, "hooks.management.flectonemaintenance.log.disable.executor")
                                + ": **" + executor + "**";

                DiscordLog.send(pluginKey, title, body);
            }

        } catch (Exception ignored) {}
    }
}
