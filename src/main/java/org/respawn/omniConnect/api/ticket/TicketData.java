package org.respawn.omniConnect.api.ticket;

import org.respawn.omniConnect.ticket.TicketType;

public class TicketData {

    private final String openerName;
    private final String openerId;
    private final TicketType type;
    private final long openedAt;

    public TicketData(String openerName, String openerId, TicketType type, long openedAt) {
        this.openerName = openerName;
        this.openerId = openerId;
        this.type = type;
        this.openedAt = openedAt;
    }

    public String getOpenerName() { return openerName; }
    public String getOpenerId() { return openerId; }
    public TicketType getType() { return type; }
    public long getOpenedAt() { return openedAt; }
}
