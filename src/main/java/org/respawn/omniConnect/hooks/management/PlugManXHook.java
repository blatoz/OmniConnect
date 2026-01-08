package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class PlugManXHook implements Listener {

    private final String pluginKey;

    public PlugManXHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] PlugManX hook aktiválva!");
    }

    @EventHandler
    public void onPlugManXEvent(Event event) {
        String name = event.getClass().getName();

        try {
            switch (name) {

                // -------------------------
                // PluginEnableEvent
                // -------------------------
                case "com.rylinaux.plugmanx.api.PluginEnableEvent": {
                    Object plugin = event.getClass().getMethod("getPlugin").invoke(event);
                    String pluginName = (String) plugin.getClass().getMethod("getName").invoke(plugin);

                    DiscordLog.send(pluginKey,
                            "🟢 PlugManX – Plugin engedélyezve",
                            "Plugin: **" + pluginName + "**"
                    );
                    break;
                }

                // -------------------------
                // PluginDisableEvent
                // -------------------------
                case "com.rylinaux.plugmanx.api.PluginDisableEvent": {
                    Object plugin = event.getClass().getMethod("getPlugin").invoke(event);
                    String pluginName = (String) plugin.getClass().getMethod("getName").invoke(plugin);

                    DiscordLog.send(pluginKey,
                            "🔴 PlugManX – Plugin letiltva",
                            "Plugin: **" + pluginName + "**"
                    );
                    break;
                }

                // -------------------------
                // PluginReloadEvent
                // -------------------------
                case "com.rylinaux.plugmanx.api.PluginReloadEvent": {
                    Object plugin = event.getClass().getMethod("getPlugin").invoke(event);
                    String pluginName = (String) plugin.getClass().getMethod("getName").invoke(plugin);

                    DiscordLog.send(pluginKey,
                            "🔄 PlugManX – Plugin újratöltve",
                            "Plugin: **" + pluginName + "**"
                    );
                    break;
                }

                default:
                    break;
            }
        } catch (Exception e) {
            Bukkit.getLogger().warning("[OmniConnect] PlugManX hook hiba: " + e.getMessage());
        }
    }
}
