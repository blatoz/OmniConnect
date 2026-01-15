package org.respawn.omniConnect.discord;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.respawn.omniConnect.commands.LinkCommand;
import org.respawn.omniConnect.lang.LangManager;
import org.respawn.omniConnect.link.LinkDatabase;

import java.util.UUID;

public class DiscordLinkVerifyListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        if (!event.getName().equalsIgnoreCase("link")) return;

        String lang = LangManager.getDefaultLanguage();

        String code = event.getOption("code").getAsString();
        UUID uuid = LinkCommand.consumeCode(code);

        if (uuid == null) {
            event.reply(LangManager.get(lang, "discord.link.invalid_code"))
                    .setEphemeral(true)
                    .queue();
            return;
        }

        LinkDatabase.link(uuid, event.getUser().getId());

        event.reply(LangManager.get(lang, "discord.link.success"))
                .setEphemeral(true)
                .queue();
    }
}
