package org.respawn.omniConnect.discord.commands.moderation;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.respawn.omniConnect.lang.LangManager;

public class PurgeCommand {

    public void execute(MessageReceivedEvent event, String[] args) {

        String lang = LangManager.getDefaultLanguage();

        if (!event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            event.getChannel().sendMessage(LangManager.get(lang, "discord.prefixmoderation.no_permission")).queue();
            return;
        }

        if (args.length < 1) {
            event.getChannel().sendMessage(LangManager.get(lang, "discord.prefixmoderation.invalid_amount")).queue();
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (Exception e) {
            event.getChannel().sendMessage(LangManager.get(lang, "discord.prefixmoderation.invalid_amount")).queue();
            return;
        }

        event.getChannel().getHistory().retrievePast(amount).queue(messages -> {
            event.getChannel().purgeMessages(messages);

            event.getChannel().sendMessage(
                    LangManager.get(lang, "discord.prefixmoderation.purge_success")
                            .replace("%amount%", String.valueOf(amount))
            ).queue();
        });
    }
}
