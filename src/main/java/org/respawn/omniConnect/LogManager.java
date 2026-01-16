package org.respawn.omniConnect;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.respawn.omniConnect.lang.LangManager;

import java.time.OffsetDateTime;
import java.util.function.Consumer;

/**
 * Logging handler - based on singleton pattern.
 * Sends embed messages to the log channel.
 */
public class LogManager {

    private static LogManager instance;

    /**
     * Singleton getInstance method.
     *
     * @return LogManager singleton instance
     */
    public static LogManager getInstance() {
        if (instance == null) {
            instance = new LogManager();
        }
        return instance;
    }

    /**
     * Send an embed message to the log channel.
     *
     * @param embedBuilderConsumer The Consumer that configures the EmbedBuilder
     */
    public void sendEmbed(Consumer<EmbedBuilder> embedBuilderConsumer) {
        TextChannel channel = getLogChannel();
        if (channel == null) return;

        EmbedBuilder embed = new EmbedBuilder();
        embedBuilderConsumer.accept(embed);

        String lang = LangManager.getDefaultLanguage();
        embed.setFooter(LangManager.get(lang, "discord.log.footer"));

        embed.setTimestamp(OffsetDateTime.now());

        channel.sendMessageEmbeds(embed.build()).queue();
    }

    /**
     * Retrieve the log channel based on the ID.
     *
     * @return TextChannel instance or null if it does not exist
     */
    private TextChannel getLogChannel() {
        // Prefer config keys present in config.yml
        String channelId = Main.getInstance().getConfig().getString("discord.channels.log");
        if (channelId == null || channelId.isEmpty()) {
            channelId = Main.getInstance().getConfig().getString("discord.channels.main");
        }
        if (channelId == null || channelId.isEmpty()) {
            channelId = Main.getInstance().getConfig().getString("discord.chat-bridge.channel-id");
        }

        if (channelId == null || channelId.isEmpty()) {
            Main.getInstance().getLogger().warning("Not has been set a log channel for discord logging feature in the config.yml!");
            return null;
        }

        try {
            if (!DiscordManager.ready) {
                Main.getInstance().getLogger().warning("Discord bot not available, log channel is not available.");
                return null;
            }

            return DiscordManager.getInstance().getJDA().getTextChannelById(channelId);
        } catch (IllegalStateException e) {
            Main.getInstance().getLogger().warning("Discord JDA not available, cannot retrieve log channel: " + e.getMessage());
            return null;
        }
    }
}
