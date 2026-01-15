package org.respawn.omniConnect.discord;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.lang.LangManager;

/**
 * Discord üzenet eseménylisszener - szinkronizálja a Discord chat üzeneteket Minecraftra.
 */
public class DiscordMessageListener extends ListenerAdapter {

    /**
     * Discord üzenet fogadása és szinkronizálása Minecraftra.
     *
     * @param event Az üzenet fogadása esemény
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (event.getAuthor().isBot()) return;

        String channelId = Main.getInstance().getConfig().getString("discord.chat.channel-id");
        if (channelId == null || !event.getChannel().getId().equals(channelId)) return;

        String lang = LangManager.getDefaultLanguage();

        String author = event.getAuthor().getName();
        String message = event.getMessage().getContentDisplay();

        String topRole = event.getMember() != null && !event.getMember().getRoles().isEmpty()
                ? event.getMember().getRoles().get(0).getName()
                : LangManager.get(lang, "minecraft.chatbridge.role_unknown");

        String prefix = LangManager.get(lang, "minecraft.chatbridge.prefix");

        String format = LangManager.get(lang, "minecraft.chatbridge.format")
                .replace("%prefix%", prefix)
                .replace("%author%", author)
                .replace("%role%", topRole)
                .replace("%message%", message);

        Bukkit.broadcastMessage(format);
    }
}
