package org.respawn.omniConnect.api;

import net.dv8tion.jda.api.JDA;
import org.respawn.omniConnect.ticket.TicketType;

public class OmniConnectAPI {

    private static JDA jda;

    public static void setJDA(JDA instance) {
        jda = instance;
    }

    public static JDA getJDA() {
        return jda;
    }

    public static boolean isDiscordReady() {
        return jda != null && jda.getStatus() == JDA.Status.CONNECTED;
    }

    // Ticket API
    public static void openTicket(String playerName, TicketType type) {
        // később TicketManager hívása
    }

    public static void closeTicket(String channelId) {
        // később TicketManager hívása
    }
}
