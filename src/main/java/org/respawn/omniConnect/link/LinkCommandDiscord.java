package org.respawn.omniConnect.link;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class LinkCommandDiscord {

    public static void handle(SlashCommandInteractionEvent event) {

        String discordId = event.getUser().getId();
        String code = String.valueOf((discordId + System.currentTimeMillis()).hashCode());

        LinkManager.getInstance().addPending(code, discordId);

        event.reply("🔗 A Minecraft szerveren írd be: `/link " + code + "`")
                .setEphemeral(true)
                .queue();
    }
}
