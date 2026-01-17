package org.respawn.omniConnect.discord.commands.moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.respawn.omniConnect.lang.LangManager;

public class WarnCommand {

    public void execute(MessageReceivedEvent event, String[] args) {

        String lang = LangManager.getDefaultLanguage();

        if (!event.getMember().hasPermission(Permission.MODERATE_MEMBERS)) {
            event.getChannel().sendMessage(LangManager.get(lang, "discord.prefixmoderation.no_permission")).queue();
            return;
        }

        if (args.length < 2) {
            event.getChannel().sendMessage(LangManager.get(lang, "discord.prefixmoderation.reason_required")).queue();
            return;
        }

        String userId = args[0];
        String reason = String.join(" ", args).substring(userId.length()).trim();

        var user = event.getJDA().getUserById(userId);
        if (user == null) {
            event.getChannel().sendMessage(LangManager.get(lang, "discord.prefixmoderation.user_not_found")).queue();
            return;
        }

        event.getChannel().sendMessage(
                LangManager.get(lang, "discord.prefixmoderation.warn_success")
                        .replace("%user%", user.getAsTag())
                        .replace("%reason%", reason)
        ).queue();
    }
}
