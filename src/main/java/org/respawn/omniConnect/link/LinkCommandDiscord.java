package org.respawn.omniConnect.link;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.respawn.omniConnect.Main;

public class LinkCommandDiscord {

    public static void handle(SlashCommandInteractionEvent event) {

        String discordId = event.getUser().getId();
        String code = String.valueOf((discordId + System.currentTimeMillis()).hashCode());

        LinkManager.getInstance().addPending(code, discordId);

        // Válasz a felhasználónak
        event.reply("🔗 A Minecraft szerveren írd be: `/link " + code + "`")
                .setEphemeral(true)
                .queue();

        // 🔥 LOG Discordra
        TextChannel log = event.getJDA().getTextChannelById(
                Main.getInstance().getConfig().getString("discord.link.log-channel-id")
        );

        if (log != null) {
            log.sendMessage("🔗 **Link kód generálva**\n"
                    + "Felhasználó: " + event.getUser().getAsTag() + "\n"
                    + "ID: " + discordId + "\n"
                    + "Kód: `" + code + "`").queue();
        }
    }
}