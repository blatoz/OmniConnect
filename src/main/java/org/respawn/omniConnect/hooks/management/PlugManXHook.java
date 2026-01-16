package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class PlugManXHook implements Listener {

    private final String pluginKey;

    public PlugManXHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] PlugManX hook has been enabled!");
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onPlugManXEvent(Event event) {
        String name = event.getClass().getName();

        try {
            String lang = lang();

            switch (name) {

                case "com.rylinaux.plugmanx.api.PluginEnableEvent": {
                    Object plugin = event.getClass().getMethod("getPlugin").invoke(event);
                    String pluginName = (String) plugin.getClass().getMethod("getName").invoke(plugin);

                    String title = LangManager.get(lang, "hooks.management.plugmanx.log.enable.title");
                    String body =
                            LangManager.get(lang, "hooks.management.plugmanx.log.enable.plugin")
                                    + ": **" + pluginName + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                case "com.rylinaux.plugmanx.api.PluginDisableEvent": {
                    Object plugin = event.getClass().getMethod("getPlugin").invoke(event);
                    String pluginName = (String) plugin.getClass().getMethod("getName").invoke(plugin);

                    String title = LangManager.get(lang, "hooks.management.plugmanx.log.disable.title");
                    String body =
                            LangManager.get(lang, "hooks.management.plugmanx.log.disable.plugin")
                                    + ": **" + pluginName + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                case "com.rylinaux.plugmanx.api.PluginReloadEvent": {
                    Object plugin = event.getClass().getMethod("getPlugin").invoke(event);
                    String pluginName = (String) plugin.getClass().getMethod("getName").invoke(plugin);

                    String title = LangManager.get(lang, "hooks.management.plugmanx.log.reload.title");
                    String body =
                            LangManager.get(lang, "hooks.management.plugmanx.log.reload.plugin")
                                    + ": **" + pluginName + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                default:
                    break;
            }

        } catch (Exception e) {
            Bukkit.getLogger().warning("[OmniConnect] PlugManX hook error: " + e.getMessage());
        }
    }
}
