package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class LinsaMaintenanceHook implements Listener {

    private final String pluginKey;

    public LinsaMaintenanceHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] LinsaFTW Maintenance hook has been enabled!");
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onLinsaMaintenanceEvent(Event event) {
        String name = event.getClass().getName();

        try {
            if (name.equals("com.linsftw.maintenance.api.events.MaintenanceToggleEvent")) {

                String lang = lang();

                boolean enabled = (boolean) event.getClass().getMethod("isEnabled").invoke(event);
                Object executorObj = event.getClass().getMethod("getExecutor").invoke(event);
                String executor = executorObj != null ? executorObj.toString() : "Ismeretlen";

                String title = LangManager.get(lang,
                        enabled
                                ? "hooks.management.linsamaintenance.log.enable.title"
                                : "hooks.management.linsamaintenance.log.disable.title"
                );

                String body =
                        LangManager.get(lang,
                                enabled
                                        ? "hooks.management.linsamaintenance.log.enable.executor"
                                        : "hooks.management.linsamaintenance.log.disable.executor"
                        ) + ": **" + executor + "**";

                DiscordLog.send(pluginKey, title, body);
            }

        } catch (Exception ignored) {}
    }
}
