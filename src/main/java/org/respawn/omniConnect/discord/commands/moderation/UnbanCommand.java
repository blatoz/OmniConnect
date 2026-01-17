package org.respawn.omniConnect.discord.commands.moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.respawn.omniConnect.lang.LangManager;

public class UnbanCommand {

    public void execute(MessageReceivedEvent event, String[] args) {

        String lang = LangManager.getDefaultLanguage();

        if (!event.getMember().hasPermission(Permission.BAN_MEMBERS)) {
            event.getChannel().sendMessage(LangManager.get(lang, "discord.prefixmoderation.no_permission")).queue();
            return;
        }

        if (args.length < 1) {
            event.getChannel().sendMessage(LangManager.get(lang, "discord.prefixmoderation.user_not_found")).queue();
            return;
        }

        String userId = args[0];

        event.getGuild()
                .unban(UserSnowflake.fromId(userId))
                .queue();

        event.getChannel().sendMessage(
                LangManager.get(lang, "discord.prefixmoderation.unban_success")
                        .replace("%user%", userId)
        ).queue();
    }
}
