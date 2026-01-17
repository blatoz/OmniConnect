package org.respawn.omniConnect.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.respawn.omniConnect.discord.PrefixStorageProvider;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.lang.LangManager;

public class ResetPrefixSlashCommand {

    public void execute(SlashCommandInteractionEvent event) {

        String lang = LangManager.getDefaultLanguage();

        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.reply(LangManager.get(lang, "discord.prefix.no_permission"))
                    .setEphemeral(true).queue();
            return;
        }

        PrefixStorageProvider.resetPrefix(event.getGuild().getId());

        String defaultPrefix = Main.getInstance().getConfig().getString("discord.prefix", "!");

        event.reply(
                LangManager.get(lang, "discord.prefix.reset")
                        .replace("%prefix%", defaultPrefix)
        ).queue();
    }
}
