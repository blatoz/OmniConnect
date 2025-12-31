package org.respawn.omniConnect.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.respawn.omniConnect.DiscordManager;
import org.respawn.omniConnect.util.RankUtil;

/**
 * Chat esemény figyelő - szinkronizálja a Minecraft chat üzeneteket Discordra.
 */
public class ChatListener implements Listener {

    /**
     * Chat esemény kezelő.
     * Feldolgozza az aszinkron chat eseményeket és továbbítja Discordra.
     *
     * @param event Az AsyncPlayerChatEvent esemény
     */
    @EventHandler
    public void onChat(final AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final String playerName = event.getPlayer().getName();
        final String message = event.getMessage();
        final String rank = RankUtil.getRank(event.getPlayer());

        // Küldés Discordra ranggal (aszinkron módon)
        new Thread(() -> DiscordManager.getInstance().sendMinecraftChat(playerName, message, rank)).start();
    }
}
