package org.respawn.omniConnect.ticket;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.link.LinkCommandDiscord;

public class TicketPanelCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        // /ticket parancs kezelése
        if (event.getName().equalsIgnoreCase("ticket")) {

            if (!event.isFromGuild()) {
                event.reply("Ezt a parancsot csak szerveren lehet használni.")
                        .setEphemeral(true)
                        .queue();
                return;
            }

            String sub = event.getSubcommandName();
            if (sub == null) return;

            switch (sub.toLowerCase()) {

                case "panel":
                    handlePanel(event);
                    break;

                case "create":
                    handleCreate(event);
                    break;

                case "staff":
                    handleStaff(event);
                    break;
            }

            return;
        }

        // 🔥 /link parancs kezelése
        if (event.getName().equalsIgnoreCase("link")) {
            LinkCommandDiscord.handle(event);
        }
    }

    // -----------------------------
    // /ticket panel
    // -----------------------------
    private void handlePanel(SlashCommandInteractionEvent event) {

        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.reply("Nincs jogosultságod a ticket panel létrehozásához.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        event.deferReply(true).queue();

        TicketManager.getInstance().sendTicketPanel(event.getJDA());
        event.getHook().sendMessage("Ticket panel elküldve a beállított csatornába.")
                .setEphemeral(true)
                .queue();
    }

    // -----------------------------
    // /ticket create
    // -----------------------------
    private void handleCreate(SlashCommandInteractionEvent event) {
        event.reply("Ticket létrehozása folyamatban...")
                .setEphemeral(true)
                .queue();

        // Itt jön majd a ticket csatorna létrehozása
    }

    // -----------------------------
    // /ticket staff
    // /ticket staff set <role>
    // -----------------------------
    private void handleStaff(SlashCommandInteractionEvent event) {

        OptionMapping roleOption = event.getOption("set");

        // Ha nincs role megadva → csak kiírjuk a jelenlegi staff role ID-t
        if (roleOption == null) {
            String staffRole = TicketConfig.getInstance().getStaffRoleId();

            event.reply("Jelenlegi staff role ID: `" + staffRole + "`")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        // Ha van role → beállítjuk
        String roleId = roleOption.getAsRole().getId();
        TicketConfig.getInstance().setStaffRoleId(roleId);
        TicketConfig.getInstance().save(Main.getInstance().getDataFolder());

        event.reply("Staff role sikeresen beállítva: <@&" + roleId + ">")
                .setEphemeral(true)
                .queue();
    }
}
