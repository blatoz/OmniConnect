package org.respawn.omniConnect.ticket;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.respawn.omniConnect.lang.LangManager;

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
        if (event.getButton() == null) return;

        String id = event.getButton().getId();
        if (id == null) return;

        String lang = LangManager.getDefaultLanguage();

        // Ticket létrehozás
        if (id.startsWith("ticket:create:")) {
            event.deferReply(true).queue();

            Member member = event.getMember();
            if (member == null) {
                event.getHook().sendMessage(LangManager.get(lang, "discord.ticket.create.error.no_member"))
                        .setEphemeral(true).queue();
                return;
            }

            TicketType type = TicketType.fromButtonId(id);
            if (type == null) {
                event.getHook().sendMessage(LangManager.get(lang, "discord.ticket.create.error.unknown_type"))
                        .setEphemeral(true).queue();
                return;
            }

            TicketManager.getInstance().createTicketChannel(event.getJDA(), member, type);
            event.getHook().sendMessage(LangManager.get(lang, "discord.ticket.create.success"))
                    .setEphemeral(true).queue();
            return;
        }

        // Ticket lezárás
        if (id.equals("ticket:close")) {
            event.deferReply(true).queue();

            if (!(event.getChannel() instanceof TextChannel)) {
                event.getHook().sendMessage(LangManager.get(lang, "discord.ticket.close.error.not_ticket_channel"))
                        .setEphemeral(true).queue();
                return;
            }

            TextChannel channel = (TextChannel) event.getChannel();
            Member closer = event.getMember();

            TicketManager.getInstance().closeTicketChannel(channel, closer);
            event.getHook().sendMessage(LangManager.get(lang, "discord.ticket.close.success"))
                    .setEphemeral(true).queue();
        }
    }
}