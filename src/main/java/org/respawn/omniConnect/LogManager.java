package org.respawn.omniConnect;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.OffsetDateTime;
import java.util.function.Consumer;

/**
 * Naplózási kezelő - singleton minta alapján.
 * Küld embed üzeneteket a log csatornára.
 */
public class LogManager {

    private static LogManager instance;

    /**
     * Singleton getInstance metódus.
     *
     * @return LogManager singleton instancia
     */
    public static LogManager getInstance() {
        if (instance == null) {
            instance = new LogManager();
        }
        return instance;
    }

    /**
     * Embed üzenet küldése a log csatornára.
     *
     * @param embedBuilderConsumer A Consumer amely az EmbedBuilder-t konfigurál
     */
    public void sendEmbed(Consumer<EmbedBuilder> embedBuilderConsumer) {
        TextChannel channel = getLogChannel();
        if (channel == null) return;

        EmbedBuilder embed = new EmbedBuilder();
        embedBuilderConsumer.accept(embed);

        embed.setFooter("OmniConnect Log Rendszer");
        embed.setTimestamp(OffsetDateTime.now());

        channel.sendMessageEmbeds(embed.build()).queue();
    }

    /**
     * A log csatorna lekérése az ID alapján.
     *
     * @return TextChannel instancia vagy null, ha nem létezik
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
            Main.getInstance().getLogger().warning("Nincs log csatorna beállítva a config.yml-ben!");
            return null;
        }

        try {
            if (!DiscordManager.ready) {
                Main.getInstance().getLogger().warning("Discord bot nem áll rendelkezésre, log csatorna nem elérhető.");
                return null;
            }

            return DiscordManager.getInstance().getJDA().getTextChannelById(channelId);
        } catch (IllegalStateException e) {
            Main.getInstance().getLogger().warning("Discord JDA nem elérhető, nem lehet log csatornát lekérni: " + e.getMessage());
            return null;
        }
    }
}
