package org.respawn.omniConnect.api.ticket;

import org.respawn.omniConnect.ticket.TicketType;

public class TicketData {

    private final String channelId;
    private final TicketType type;
    private final String user;

    public TicketData(String channelId, TicketType type, String user) {
        this.channelId = channelId;
        this.type = type;
        this.user = user;
    }

    public String getChannelId() { return channelId; }
    public TicketType getType() { return type; }
    public String getUser() { return user; }
}
