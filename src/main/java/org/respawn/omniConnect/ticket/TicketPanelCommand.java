package org.respawn.omniConnect.ticket;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.respawn.omniConnect.lang.LangManager;

public class TicketPanelCommand extends ListenerAdapter {

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

        // Küldjük a paneleket típusonként
        for (TicketType type : TicketType.values()) {
            var channel = TicketManager.getInstance().getPanelChannel(event.getJDA(), type);

            if (channel == null) {
                event.getHook().sendMessage(
                        LangManager.get(lang, "discord.ticket.panel.missing_channel")
                                .replace("%type%", type.name())
                ).setEphemeral(true).queue();
                continue;
            }

            TicketManager.getInstance().sendTicketPanel(event.getJDA(), type);
        }

        event.getHook().sendMessage(LangManager.get(lang, "discord.ticket.panel.sent"))
                .setEphemeral(true).queue();
    }
}
