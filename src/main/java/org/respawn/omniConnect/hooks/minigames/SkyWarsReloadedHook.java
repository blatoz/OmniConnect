package org.respawn.omniConnect.hooks.minigames;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.lang.LangManager;
import org.respawn.omniConnect.hooks.DiscordLog;

import java.lang.reflect.Method;

public class SkyWarsReloadedHook implements Listener {

    private Class<?> winEventClass;
    private Method getPlayerMethod;

    private boolean initialized = false;

    public SkyWarsReloadedHook() {
        try {
            winEventClass = Class.forName("com.walrusone.skywars.events.SkyWarsWinEvent");
            getPlayerMethod = winEventClass.getMethod("getPlayer");

            Bukkit.getPluginManager().registerEvents(this, Main.getInstance());

            initialized = true;
            Main.getInstance().getLogger().info("[OmniConnect] SkyWarsReloaded hook inicializálva (reflection).");

        } catch (Exception e) {
            Main.getInstance().getLogger().warning("[OmniConnect] SkyWarsReloaded hook nem inicializálható: " + e.getMessage());
        }
    }

    @EventHandler
    public void onSkyWarsWin(Event event) {
        if (!initialized) return;
        if (!winEventClass.isInstance(event)) return;

        try {
            Player winner = (Player) getPlayerMethod.invoke(event);

            String lang = LangManager.getDefaultLanguage();

            String title = LangManager.get(lang, "discord.minigames.skywars.title");
            String description = LangManager.get(lang, "discord.minigames.skywars.game_end")
                    .replace("%winner%", winner.getName());

            DiscordLog.send(title, description);

            Main.getInstance().getLogger().info(
                    "[OmniConnect] SkyWars Win | Winner=" + winner.getName()
            );

        } catch (Exception e) {
            Main.getInstance().getLogger().warning("[OmniConnect] Hiba a SkyWarsReloaded WinEvent feldolgozásakor: " + e.getMessage());
        }
    }
}
