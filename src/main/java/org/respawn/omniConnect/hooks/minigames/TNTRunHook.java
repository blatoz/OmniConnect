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

public class TNTRunHook implements Listener {

    private Class<?> gameEndEventClass;
    private Method getWinnerMethod;
    private Method getArenaNameMethod;

    private boolean initialized = false;

    public TNTRunHook() {
        try {
            gameEndEventClass = Class.forName("me.mrCookieSlime.TNTRun.api.events.TNTRunGameEndEvent");
            getWinnerMethod = gameEndEventClass.getMethod("getWinner");
            getArenaNameMethod = gameEndEventClass.getMethod("getArenaName");

            Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
            initialized = true;

            Main.getInstance().getLogger().info("[OmniConnect] TNTRun hook inicializálva (reflection).");

        } catch (Exception e) {
            Main.getInstance().getLogger().warning("[OmniConnect] TNTRun hook nem inicializálható: " + e.getMessage());
        }
    }

    @EventHandler
    public void onTNTRunEnd(Event event) {
        if (!initialized) return;
        if (!gameEndEventClass.isInstance(event)) return;

        try {
            Player winner = (Player) getWinnerMethod.invoke(event);
            String arena = (String) getArenaNameMethod.invoke(event);

            String lang = LangManager.getDefaultLanguage();

            String title = LangManager.get(lang, "discord.minigames.tntrun.title");
            String description = LangManager.get(lang, "discord.minigames.tntrun.game_end")
                    .replace("%arena%", arena)
                    .replace("%winner%", winner.getName());

            DiscordLog.send(title, description);

        } catch (Exception ignored) {}
    }
}
