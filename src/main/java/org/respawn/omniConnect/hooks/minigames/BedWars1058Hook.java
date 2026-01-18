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
import java.util.List;

public class BedWars1058Hook implements Listener {

    private Class<?> gameEndEventClass;
    private Method getWinnersMethod;
    private Method getArenaMethod;
    private Method getArenaNameMethod;

    private boolean initialized = false;

    public BedWars1058Hook() {
        try {
            // Event osztály reflectionnel
            gameEndEventClass = Class.forName("com.andrei1058.bedwars.api.events.gameplay.GameEndEvent");

            // Metódusok reflectionnel
            getWinnersMethod = gameEndEventClass.getMethod("getWinners");
            getArenaMethod = gameEndEventClass.getMethod("getArena");

            Class<?> arenaClass = Class.forName("com.andrei1058.bedwars.api.arena.IArena");
            getArenaNameMethod = arenaClass.getMethod("getArenaName");

            // Listener regisztrálása
            Bukkit.getPluginManager().registerEvents(this, Main.getInstance());

            initialized = true;
            Main.getInstance().getLogger().info("[OmniConnect] BedWars1058 hook initialized (reflection).");

        } catch (Exception e) {
            Main.getInstance().getLogger().warning("[OmniConnect] BedWars1058 hook is not initialized: " + e.getMessage());
        }
    }

    @EventHandler
    public void onGameEnd(Event event) {
        if (!initialized) return;
        if (!gameEndEventClass.isInstance(event)) return;

        try {
            Object winnersObj = getWinnersMethod.invoke(event);
            Object arenaObj = getArenaMethod.invoke(event);

            String arenaName = (String) getArenaNameMethod.invoke(arenaObj);

            @SuppressWarnings("unchecked")
            List<Player> winners = (List<Player>) winnersObj;

            StringBuilder sb = new StringBuilder();
            for (Player p : winners) {
                if (!sb.isEmpty()) sb.append(", ");
                sb.append(p.getName());
            }

            String winnersStr = sb.isEmpty() ? "-" : sb.toString();

            // --- Nyelvi rendszer ---
            String lang = LangManager.getDefaultLanguage();

            String title = LangManager.get(lang, "hooks.minigames.bedwars.title");
            String description = LangManager.get(lang, "hooks.minigames.bedwars.game_end")
                    .replace("%arena%", arenaName)
                    .replace("%winners%", winnersStr);

            // --- DiscordLog használata ---
            DiscordLog.send(
                    title,
                    description
            );

            // Konzol log
            Main.getInstance().getLogger().info(
                    "[OmniConnect] BedWars GameEnd | Arena=" + arenaName + " | Winners=" + winnersStr
            );

        } catch (Exception e) {
            Main.getInstance().getLogger().warning("[OmniConnect] Error when BedWars1058 GameEndEvent during processing: " + e.getMessage());
        }
    }
}
