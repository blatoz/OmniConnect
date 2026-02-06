package org.respawn.omniConnect.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChatBridgeMessageEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final String sender;
    private final String message;
    public ChatBridgeMessageEvent(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }
    public String getSender() { return sender; }
    public String getMessage() { return message; }
    @Override public HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() { return handlers; }
}

