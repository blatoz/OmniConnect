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

public class MurderMysteryHook implements Listener {

    private Class<?> gameEndEventClass;
    private Method getWinnersMethod;
    private Method getArenaMethod;
    private Method getNameMethod;

    private boolean initialized = false;

    public MurderMysteryHook() {
        try {
            gameEndEventClass = Class.forName("plugily.projects.murdermystery.api.events.game.GameEndEvent");
            getWinnersMethod = gameEndEventClass.getMethod("getWinners");
            getArenaMethod = gameEndEventClass.getMethod("getArena");

            Class<?> arenaClass = Class.forName("plugily.projects.murdermystery.arena.Arena");
            getNameMethod = arenaClass.getMethod("getMapName");

            Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
            initialized = true;

            Main.getInstance().getLogger().info("[OmniConnect] MurderMystery hook inicializálva (reflection).");

        } catch (Exception e) {
            Main.getInstance().getLogger().warning("[OmniConnect] MurderMystery hook nem inicializálható: " + e.getMessage());
        }
    }

    @EventHandler
    public void onMMEnd(Event event) {
        if (!initialized) return;
        if (!gameEndEventClass.isInstance(event)) return;

        try {
            @SuppressWarnings("unchecked")
            java.util.List<Player> winners = (java.util.List<Player>) getWinnersMethod.invoke(event);

            Object arenaObj = getArenaMethod.invoke(event);
            String arenaName = (String) getNameMethod.invoke(arenaObj);

            String winnersStr = winners.isEmpty()
                    ? "-"
                    : String.join(", ", winners.stream().map(Player::getName).toList());

            String lang = LangManager.getDefaultLanguage();

            String title = LangManager.get(lang, "discord.minigames.murdermystery.title");
            String description = LangManager.get(lang, "discord.minigames.murdermystery.game_end")
                    .replace("%arena%", arenaName)
                    .replace("%winners%", winnersStr);

            DiscordLog.send(title, description);

        } catch (Exception ignored) {}
    }
}
