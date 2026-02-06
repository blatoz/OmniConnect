package org.respawn.omniConnect.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ModerationLogEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final String action;
    private final String target;
    private final String moderator;
    public ModerationLogEvent(String action, String target, String moderator) {
        this.action = action;
        this.target = target;
        this.moderator = moderator;
    }
    public String getAction() { return action; }
    public String getTarget() { return target; }
    public String getModerator() { return moderator; }
    @Override public HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() { return handlers; }
}

