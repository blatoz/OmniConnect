package org.respawn.omniConnect.hooks.economy;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class AzrovaEconomyHook implements Listener {

    private final String pluginKey;

    public AzrovaEconomyHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onAzrovaEco(Event event) {
        if (!event.getClass().getName().equals("me.azrova.economy.api.EconomyChangeEvent"))
            return;

        try {
            String lang = lang();

            Object player = event.getClass().getMethod("getPlayer").invoke(event);
            double oldBal = (double) event.getClass().getMethod("getOldBalance").invoke(event);
            double newBal = (double) event.getClass().getMethod("getNewBalance").invoke(event);

            String name = (String) player.getClass().getMethod("getName").invoke(player);
            double diff = newBal - oldBal;

            String title = LangManager.get(lang, "hooks.economy.azrova.log.change.title");

            String body =
                    LangManager.get(lang, "hooks.economy.azrova.log.change.player") + ": **" + name + "**\n" +
                            LangManager.get(lang, "hooks.economy.azrova.log.change.diff") + ": **" + diff + "**\n" +
                            LangManager.get(lang, "hooks.economy.azrova.log.change.new_balance") + ": **" + newBal + "**";

            DiscordLog.send(pluginKey, title, body);

        } catch (Exception ignored) {}
    }
}
