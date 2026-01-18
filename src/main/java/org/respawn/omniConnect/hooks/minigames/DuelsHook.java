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

public class DuelsHook implements Listener {

    private Class<?> duelEndEventClass;
    private Method getWinnerMethod;
    private Method getLoserMethod;

    private boolean initialized = false;

    public DuelsHook() {
        try {
            duelEndEventClass = Class.forName("me.realized.duels.api.event.DuelEndEvent");
            getWinnerMethod = duelEndEventClass.getMethod("getWinner");
            getLoserMethod = duelEndEventClass.getMethod("getLoser");

            Bukkit.getPluginManager().registerEvents(this, Main.getInstance());

            initialized = true;
            Main.getInstance().getLogger().info("[OmniConnect] Duels hook inicializálva (reflection).");

        } catch (Exception e) {
            Main.getInstance().getLogger().warning("[OmniConnect] Duels hook nem inicializálható: " + e.getMessage());
        }
    }

    @EventHandler
    public void onDuelEnd(Event event) {
        if (!initialized) return;
        if (!duelEndEventClass.isInstance(event)) return;

        try {
            Player winner = (Player) getWinnerMethod.invoke(event);
            Player loser = (Player) getLoserMethod.invoke(event);

            String lang = LangManager.getDefaultLanguage();

            String title = LangManager.get(lang, "discord.minigames.duels.title");
            String description = LangManager.get(lang, "discord.minigames.duels.game_end")
                    .replace("%winner%", winner.getName())
                    .replace("%loser%", loser.getName());

            DiscordLog.send(title, description);

            Main.getInstance().getLogger().info(
                    "[OmniConnect] Duel End | Winner=" + winner.getName() + " | Loser=" + loser.getName()
            );

        } catch (Exception e) {
            Main.getInstance().getLogger().warning("[OmniConnect] Hiba a Duels DuelEndEvent feldolgozásakor: " + e.getMessage());
        }
    }
}
