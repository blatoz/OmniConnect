package org.respawn.omniConnect.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DiscordReadyEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
