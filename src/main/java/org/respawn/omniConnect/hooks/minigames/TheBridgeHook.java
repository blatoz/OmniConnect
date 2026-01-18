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

public class TheBridgeHook implements Listener {

    private Class<?> gameEndEventClass;
    private Method getWinnerMethod;
    private Method getArenaMethod;
    private Method getArenaNameMethod;

    private boolean initialized = false;

    public TheBridgeHook() {
        try {
            gameEndEventClass = Class.forName("com.andrei1058.thebridge.api.events.GameEndEvent");
            getWinnerMethod = gameEndEventClass.getMethod("getWinner");
            getArenaMethod = gameEndEventClass.getMethod("getArena");

            Class<?> arenaClass = Class.forName("com.andrei1058.thebridge.api.arena.IArena");
            getArenaNameMethod = arenaClass.getMethod("getArenaName");

            Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
            initialized = true;

            Main.getInstance().getLogger().info("[OmniConnect] TheBridge hook inicializálva (reflection).");

        } catch (Exception e) {
            Main.getInstance().getLogger().warning("[OmniConnect] TheBridge hook nem inicializálható: " + e.getMessage());
        }
    }

    @EventHandler
    public void onBridgeEnd(Event event) {
        if (!initialized) return;
        if (!gameEndEventClass.isInstance(event)) return;

        try {
            Player winner = (Player) getWinnerMethod.invoke(event);
            Object arenaObj = getArenaMethod.invoke(event);
            String arenaName = (String) getArenaNameMethod.invoke(arenaObj);

            String lang = LangManager.getDefaultLanguage();

            String title = LangManager.get(lang, "discord.minigames.thebridge.title");
            String description = LangManager.get(lang, "discord.minigames.thebridge.game_end")
                    .replace("%arena%", arenaName)
                    .replace("%winner%", winner.getName());

            DiscordLog.send(title, description);

        } catch (Exception ignored) {}
    }
}
