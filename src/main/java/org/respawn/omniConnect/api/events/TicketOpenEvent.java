package org.respawn.omniConnect.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.respawn.omniConnect.api.ticket.TicketData;

public class TicketOpenEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final TicketData data;
    public TicketOpenEvent(TicketData data) { this.data = data; }
    public TicketData getTicketData() { return data; }
    @Override public HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() { return handlers; }
}

