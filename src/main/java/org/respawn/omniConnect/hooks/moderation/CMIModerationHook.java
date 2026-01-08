package org.respawn.omniConnect.hooks.moderation;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class CMIModerationHook implements Listener {

    private final String pluginKey;

    public CMIModerationHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onCMIEvent(Event event) {
        String name = event.getClass().getName();

        try {
            switch (name) {

                case "com.Zrips.CMI.events.CMIBanEvent": {
                    Object banned = event.getClass().getMethod("getBanned").invoke(event);
                    String target = (String) banned.getClass().getMethod("getName").invoke(banned);

                    DiscordLog.send(pluginKey, "⛔ CMI Kitiltás", "Játékos: **" + target + "**");
                    break;
                }

                case "com.Zrips.CMI.events.CMIMuteEvent": {
                    Object muted = event.getClass().getMethod("getMuted").invoke(event);
                    String target = (String) muted.getClass().getMethod("getName").invoke(muted);

                    DiscordLog.send(pluginKey, "🔇 CMI Némitás", "Játékos: **" + target + "**");
                    break;
                }

                case "com.Zrips.CMI.events.CMIKickEvent": {
                    Object kicked = event.getClass().getMethod("getKicked").invoke(event);
                    String target = (String) kicked.getClass().getMethod("getName").invoke(kicked);

                    DiscordLog.send(pluginKey, "👢 CMI Kirúgás", "Játékos: **" + target + "**");
                    break;
                }

                case "com.Zrips.CMI.events.CMIJailEvent": {
                    Object jailed = event.getClass().getMethod("getJailed").invoke(event);
                    String target = (String) jailed.getClass().getMethod("getName").invoke(jailed);

                    DiscordLog.send(pluginKey, "🚨 CMI Bebörtönzés", "Játékos: **" + target + "**");
                    break;
                }

                case "com.Zrips.CMI.events.CMIUnjailEvent": {
                    Object jailed = event.getClass().getMethod("getJailed").invoke(event);
                    String target = (String) jailed.getClass().getMethod("getName").invoke(jailed);

                    DiscordLog.send(pluginKey, "🔓 CMI Bebörtönzés Törölve", "Játékos: **" + target + "**");
                    break;
                }

                default:
                    break;
            }

        } catch (Exception ignored) {}
    }
}
