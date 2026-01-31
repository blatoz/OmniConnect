package org.respawn.omniConnect.api.ticket;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.respawn.omniConnect.ticket.TicketManager;
import org.respawn.omniConnect.ticket.TicketType;

public class TicketAPI {

    /**
     * Ticket nyitása Discord oldalról (külső pluginból).
     */
    public static void openTicket(Member member, TicketType type) {
        TicketManager.getInstance().createTicketChannel(member.getJDA(), member, type);
    }

    /**
     * Ticket lezárása.
     */
    public static void closeTicket(TextChannel channel, Member closer) {
        TicketManager.getInstance().closeTicketChannel(channel, closer);
    }

    /**
     * Ticket típus lekérése csatornanévből.
     */
    public static TicketType getTicketType(TextChannel channel) {
        return org.respawn.omniConnect.ticket.TicketTypeResolver.resolve(channel.getName());
    }
}
