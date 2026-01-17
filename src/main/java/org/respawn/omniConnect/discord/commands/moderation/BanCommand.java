package org.respawn.omniConnect.discord.commands.moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.respawn.omniConnect.lang.LangManager;

public class BanCommand {

    public void execute(MessageReceivedEvent event, String[] args) {

        String lang = LangManager.getDefaultLanguage();

        if (!event.getMember().hasPermission(Permission.BAN_MEMBERS)) {
            event.getChannel().sendMessage(LangManager.get(lang, "discord.moderation.no_permission")).queue();
            return;
        }

        if (args.length < 2) {
            event.getChannel().sendMessage(LangManager.get(lang, "discord.moderation.reason_required")).queue();
            return;
        }

        String userId = args[0];
        String reason = String.join(" ", args).substring(userId.length()).trim();

        event.getGuild()
                .ban(UserSnowflake.fromId(userId), 0, reason)
                .queue();

        event.getChannel().sendMessage(
                LangManager.get(lang, "discord.moderation.ban_success")
                        .replace("%user%", userId)
                        .replace("%reason%", reason)
        ).queue();
    }
}
