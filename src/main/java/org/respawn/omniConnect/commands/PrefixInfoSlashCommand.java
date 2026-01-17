package org.respawn.omniConnect.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.respawn.omniConnect.discord.PrefixStorageProvider;
import org.respawn.omniConnect.lang.LangManager;

public class PrefixInfoSlashCommand {

    public void execute(SlashCommandInteractionEvent event) {

        String lang = LangManager.getDefaultLanguage();

        String prefix = PrefixStorageProvider.getPrefix(event.getGuild().getId());

        event.reply(
                LangManager.get(lang, "discord.prefix.current")
                        .replace("%prefix%", prefix)
        ).queue();
    }
}
