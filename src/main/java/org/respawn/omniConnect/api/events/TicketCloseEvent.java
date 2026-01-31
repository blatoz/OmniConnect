package org.respawn.omniConnect.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.respawn.omniConnect.api.ticket.TicketData;

public class TicketCloseEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final TicketData data;

    public TicketCloseEvent(TicketData data) {
        this.data = data;
    }

    public TicketData getTicketData() {
        return data;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
