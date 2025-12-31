package org.respawn.omniConnect.ticket;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.respawn.omniConnect.Main;

import java.util.EnumSet;

/**
 * Jegy (ticket) eseménylisszener - kezeli a ticket gombok interakcióit.
 */
public class TicketListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        if (event.getButton() == null) return;

        String id = event.getButton().getId();
        if (id == null) return;

        // -----------------------------
        // TICKET LÉTREHOZÁS
        // -----------------------------
        if (id.startsWith("ticket:create:")) {

            event.deferReply(true).queue(); // ephemeral válasz

            Member member = event.getMember();
            if (member == null) {
                event.getHook().sendMessage("Nem sikerült betölteni a felhasználói adataidat.")
                        .setEphemeral(true).queue();
                return;
            }

            Guild guild = event.getGuild();
            if (guild == null) return;

            // Ticket kategória ID config.yml-ből
            String categoryId = Main.getInstance().getConfig().getString("discord.ticket.category-id");
            Category category = guild.getCategoryById(categoryId);

            if (category == null) {
                event.getHook().sendMessage("❌ A ticket kategória nem található!")
                        .setEphemeral(true).queue();
                return;
            }

            // Staff role ID ticketconfig.json-ből
            String staffRoleId = TicketConfig.getInstance().getStaffRoleId();

            // Csatorna neve
            String channelName = "ticket-" + member.getUser().getName().toLowerCase();

            guild.createTextChannel(channelName, category)
                    .addPermissionOverride(
                            guild.getPublicRole(),
                            null,
                            EnumSet.of(Permission.VIEW_CHANNEL)
                    )
                    .addPermissionOverride(
                            guild.getRoleById(staffRoleId),
                            EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND),
                            null
                    )
                    .addPermissionOverride(
                            member,
                            EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND),
                            null
                    )
                    .queue(channel -> {

                        channel.sendMessage("🎫 **Ticket létrehozva!**\n" +
                                "A staff hamarosan segít neked.").queue();

                        event.getHook().sendMessage("A ticket csatornád létrejött: " + channel.getAsMention())
                                .setEphemeral(true)
                                .queue();
                    });

            return;
        }

        // -----------------------------
        // TICKET LEZÁRÁS
        // -----------------------------
        if (id.equals("ticket:close")) {

            event.deferReply(true).queue();

            if (!(event.getChannel() instanceof TextChannel)) {
                event.getHook().sendMessage("Ez a gomb csak ticket csatornában működik.")
                        .setEphemeral(true).queue();
                return;
            }

            TextChannel channel = (TextChannel) event.getChannel();
            Member closer = event.getMember();

            TicketManager.getInstance().closeTicketChannel(channel, closer);

            event.getHook().sendMessage("A ticket lezárásra került.")
                    .setEphemeral(true).queue();
        }
    }
}
