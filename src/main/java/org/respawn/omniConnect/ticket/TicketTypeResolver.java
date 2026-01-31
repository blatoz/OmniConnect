package org.respawn.omniConnect.ticket;

public class TicketTypeResolver {

    public static TicketType resolve(String channelName) {
        if (channelName == null) return null;

        String lower = channelName.toLowerCase();

        for (TicketType type : TicketType.values()) {
            String prefix = type.getChannelPrefix().toLowerCase();
            if (lower.startsWith(prefix + "-") || lower.equals(prefix)) {
                return type;
            }
        }
        return null;
    }
}
