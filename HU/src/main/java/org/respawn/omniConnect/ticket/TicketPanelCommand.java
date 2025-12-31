package org.respawn.omniConnect.ticket;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

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
        if (!event.getName().equalsIgnoreCase("ticketpanel")) {
            return;
        }

        if (!event.isFromGuild()) {
            event.reply("Ezt a parancsot csak szerveren lehet használni.").setEphemeral(true).queue();
            return;
        }

        // opcionálisan: jogosultság ellenőrzés (pl. ADMINISTRATOR)
        if (event.getMember() == null ||
                !event.getMember().hasPermission(net.dv8tion.jda.api.Permission.ADMINISTRATOR)) {
            event.reply("Nincs jogosultságod a ticket panel létrehozásához.").setEphemeral(true).queue();
            return;
        }

        event.deferReply(true).queue();

        TicketManager.getInstance().sendTicketPanel(event.getJDA());
        event.getHook().sendMessage("Ticket panel elküldve a beállított csatornába.").setEphemeral(true).queue();
    }
}
