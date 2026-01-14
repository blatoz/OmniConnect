package org.respawn.omniConnect.ticket;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.respawn.omniConnect.lang.LangManager;

/**
 * Ticket panel parancs kezelő - kezeli a /ticketpanel slash parancsot.
 */
public class TicketPanelCommand extends ListenerAdapter {

    /**
     * Slash parancs interakció kezelő.
     *
     * @param event A slash parancs interakció eseménye
     */
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equalsIgnoreCase("ticketpanel")) return;

        String lang = LangManager.getDefaultLanguage();

        if (!event.isFromGuild()) {
            event.reply(LangManager.get(lang, "discord.ticket.panel.only_guild"))
                    .setEphemeral(true).queue();
            return;
        }

        if (event.getMember() == null ||
                !event.getMember().hasPermission(net.dv8tion.jda.api.Permission.ADMINISTRATOR)) {
            event.reply(LangManager.get(lang, "discord.ticket.panel.no_permission"))
                    .setEphemeral(true).queue();
            return;
        }

        event.deferReply(true).queue();

        TicketManager.getInstance().sendTicketPanel(event.getJDA());
        event.getHook().sendMessage(LangManager.get(lang, "discord.ticket.panel.sent"))
                .setEphemeral(true).queue();
    }
}