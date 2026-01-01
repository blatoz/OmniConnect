package org.respawn.omniConnect.ticket;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * Jegy (ticket) eseménylisszener - kezeli a ticket gombok interakcióit.
 */
public class TicketListener extends ListenerAdapter {

    /**
     * Gomb interakció kezelő - kezeli a ticket létrehozást és bezárást.
     *
     * @param event A gomb interakció eseménye
     */
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getButton() == null) {
            return;
        }

        String id = event.getButton().getId();
        if (id == null) {
            return;
        }

        // Ticket létrehozás gombok: ticket:create:<TYPE>
        if (id.startsWith("ticket:create:")) {
            event.deferReply(true).queue(); // ephemeral válasz

            Member member = event.getMember();
            if (member == null) {
                event.getHook().sendMessage("Nem sikerült betölteni a felhasználói adataidat.").setEphemeral(true).queue();
                return;
            }

            TicketType type = TicketType.fromButtonId(id);
            if (type == null) {
                event.getHook().sendMessage("Ismeretlen ticket típus.").setEphemeral(true).queue();
                return;
            }

            TicketManager.getInstance().createTicketChannel(event.getJDA(), member, type);
            event.getHook().sendMessage("A ticket csatornád létrejött.").setEphemeral(true).queue();
            return;
        }

        // Ticket lezárása: ticket:close
        if (id.equals("ticket:close")) {
            event.deferReply(true).queue();

            if (!(event.getChannel() instanceof TextChannel)) {
                event.getHook().sendMessage("Ez a gomb csak ticket szövegcsatornában működik.").setEphemeral(true).queue();
                return;
            }

            TextChannel channel = (TextChannel) event.getChannel();
            Member closer = event.getMember();

            TicketManager.getInstance().closeTicketChannel(channel, closer);
            event.getHook().sendMessage("A ticket lezárásra került.").setEphemeral(true).queue();
        }
    }
}
