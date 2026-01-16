package org.respawn.omniConnect.hooks.economy;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class PlayerPointsHook implements Listener {

    private final String pluginKey;

    public PlayerPointsHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onPointsEvent(Event event) {
        String name = event.getClass().getName();

        try {
            String lang = lang();

            switch (name) {

                case "org.black_ixx.playerpoints.event.PlayerPointsChangeEvent": {
                    Object player = event.getClass().getMethod("getPlayer").invoke(event);
                    int amount = (int) event.getClass().getMethod("getChange").invoke(event);
                    int newBalance = (int) event.getClass().getMethod("getNewBalance").invoke(event);

                    String playerName = (String) player.getClass().getMethod("getName").invoke(player);

                    String title = LangManager.get(lang, "hooks.economy.playerpoints.log.change.title");

                    String body =
                            LangManager.get(lang, "hooks.economy.playerpoints.log.change.player") + ": **" + playerName + "**\n" +
                                    LangManager.get(lang, "hooks.economy.playerpoints.log.change.diff") + ": **" + amount + "**\n" +
                                    LangManager.get(lang, "hooks.economy.playerpoints.log.change.new_balance") + ": **" + newBalance + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                case "org.black_ixx.playerpoints.event.PlayerPointsResetEvent": {
                    Object player = event.getClass().getMethod("getPlayer").invoke(event);
                    String playerName = (String) player.getClass().getMethod("getName").invoke(player);

                    String title = LangManager.get(lang, "hooks.economy.playerpoints.log.reset.title");

                    String body =
                            LangManager.get(lang, "hooks.economy.playerpoints.log.reset.player") + ": **" + playerName + "** " +
                                    LangManager.get(lang, "hooks.economy.playerpoints.log.reset.message");

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                default:
                    break;
            }

        } catch (Exception ignored) {}
    }
}
