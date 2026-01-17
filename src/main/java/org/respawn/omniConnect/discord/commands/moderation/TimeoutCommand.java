package org.respawn.omniConnect.discord.commands.moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.respawn.omniConnect.lang.LangManager;

import java.time.Duration;

public class TimeoutCommand {

    public void execute(MessageReceivedEvent event, String[] args) {

        String lang = LangManager.getDefaultLanguage();

        if (!event.getMember().hasPermission(Permission.MODERATE_MEMBERS)) {
            event.getChannel().sendMessage(LangManager.get(lang, "discord.prefixmoderation.no_permission")).queue();
            return;
        }

        if (args.length < 3) {
            event.getChannel().sendMessage(LangManager.get(lang, "discord.prefixmoderation.duration_required")).queue();
            return;
        }

        String userId = args[0];
        String durationStr = args[1];
        String reason = String.join(" ", args).substring(userId.length() + durationStr.length()).trim();

        var member = event.getGuild().getMemberById(userId);
        if (member == null) {
            event.getChannel().sendMessage(LangManager.get(lang, "discord.prefixmoderation.user_not_found")).queue();
            return;
        }

        Duration duration = Duration.parse("PT" + durationStr.toUpperCase());

        member.timeoutFor(duration).queue();

        event.getChannel().sendMessage(
                LangManager.get(lang, "discord.prefixmoderation.timeout_success")
                        .replace("%user%", member.getUser().getAsTag())
                        .replace("%duration%", durationStr)
                        .replace("%reason%", reason)
        ).queue();
    }
}
