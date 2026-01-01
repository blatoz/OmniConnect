package org.respawn.omniConnect.hooks.moderation;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class EssentialsXModerationHook implements Listener {

    private final String pluginKey;

    public EssentialsXModerationHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onEssentialsModeration(Event event) {
        String name = event.getClass().getName();

        try {
            switch (name) {

                case "com.earth2me.essentials.events.EssentialsBanEvent": {
                    Object player = event.getClass().getMethod("getBan").invoke(event);
                    String target = (String) player.getClass().getMethod("getName").invoke(player);

                    DiscordLog.send(pluginKey,
                            "⛔ EssentialsX Ban",
                            "Játékos: **" + target + "**"
                    );
                    break;
                }

                case "com.earth2me.essentials.events.EssentialsTempBanEvent": {
                    Object ban = event.getClass().getMethod("getBan").invoke(event);
                    String target = (String) ban.getClass().getMethod("getName").invoke(ban);

                    DiscordLog.send(pluginKey,
                            "⏳ EssentialsX TempBan",
                            "Játékos: **" + target + "**"
                    );
                    break;
                }

                case "com.earth2me.essentials.events.EssentialsMuteEvent": {
                    Object mute = event.getClass().getMethod("getMute").invoke(event);
                    String target = (String) mute.getClass().getMethod("getName").invoke(mute);

                    DiscordLog.send(pluginKey,
                            "🔇 EssentialsX Mute",
                            "Játékos: **" + target + "**"
                    );
                    break;
                }

                case "com.earth2me.essentials.events.EssentialsUnmuteEvent": {
                    Object mute = event.getClass().getMethod("getMute").invoke(event);
                    String target = (String) mute.getClass().getMethod("getName").invoke(mute);

                    DiscordLog.send(pluginKey,
                            "🔊 EssentialsX Unmute",
                            "Játékos: **" + target + "**"
                    );
                    break;
                }

                case "com.earth2me.essentials.events.EssentialsKickEvent": {
                    Object kicked = event.getClass().getMethod("getKicked").invoke(event);
                    String target = (String) kicked.getClass().getMethod("getName").invoke(kicked);

                    DiscordLog.send(pluginKey,
                            "👢 EssentialsX Kick",
                            "Játékos: **" + target + "**"
                    );
                    break;
                }

                default:
                    break;
            }

        } catch (Exception ignored) {}
    }
}
