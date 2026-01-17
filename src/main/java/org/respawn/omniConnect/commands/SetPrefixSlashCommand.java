package org.respawn.omniConnect.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.respawn.omniConnect.discord.PrefixStorageProvider;
import org.respawn.omniConnect.lang.LangManager;

public class SetPrefixSlashCommand {

    public void execute(SlashCommandInteractionEvent event) {

        String lang = LangManager.getDefaultLanguage();

        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.reply(LangManager.get(lang, "discord.prefix.no_permission"))
                    .setEphemeral(true).queue();
            return;
        }

        String newPrefix = event.getOption("prefix").getAsString();

        if (newPrefix == null || newPrefix.trim().isEmpty()) {
            event.reply(LangManager.get(lang, "discord.prefix.invalid"))
                    .setEphemeral(true).queue();
            return;
        }

        PrefixStorageProvider.setPrefix(event.getGuild().getId(), newPrefix);

        event.reply(
                LangManager.get(lang, "discord.prefix.updated")
                        .replace("%prefix%", newPrefix)
        ).queue();
    }
}
