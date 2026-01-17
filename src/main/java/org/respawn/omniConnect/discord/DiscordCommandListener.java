package org.respawn.omniConnect.discord;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.respawn.omniConnect.discord.commands.moderation.*;

public class DiscordCommandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (event.getAuthor().isBot()) return;
        if (!event.isFromGuild()) return;

        String prefix = PrefixStorageProvider.getPrefix(event.getGuild().getId());
        String msg = event.getMessage().getContentRaw();

        if (!msg.startsWith(prefix)) return;

        // Remove prefix
        String withoutPrefix = msg.substring(prefix.length()).trim();

        // Split command + args
        String[] split = withoutPrefix.split(" ");
        String cmdName = split[0].toLowerCase();

        // Arguments (everything after the command name)
        String[] args = new String[Math.max(0, split.length - 1)];
        if (split.length > 1) {
            System.arraycopy(split, 1, args, 0, split.length - 1);
        }

        switch (cmdName) {

            case "warn":
                new WarnCommand().execute(event, args);
                break;

            case "kick":
                new KickCommand().execute(event, args);
                break;

            case "ban":
                new BanCommand().execute(event, args);
                break;

            case "timeout":
                new TimeoutCommand().execute(event, args);
                break;

            case "purge":
                new PurgeCommand().execute(event, args);
                break;

            case "unban":
                new UnbanCommand().execute(event, args);
                break;

            default:
                // Nem moderációs parancs → ignoráljuk
                return;
        }
    }
}
