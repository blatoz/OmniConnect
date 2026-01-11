package org.respawn.omniConnect.hooks;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.respawn.omniConnect.DiscordManager;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.config.OmniConfig;

import java.awt.*;

public class DiscordLog {

    /**
     * Általános logolás pluginKey alapján (régi rendszer kompatibilitás).
     * anticheat / exploitfix hookokhoz.
     */
    public static void send(String pluginKey, String title, String description) {
        if (!DiscordManager.ready) return;

        var cfg = Main.getInstance().getConfig();

        // 1) Anticheat specifikus csatorna
        String specific = cfg.getString("anticheat." + pluginKey + ".log-channel");

        // 2) Ha nincs → exploitfix
        if (specific == null || specific.isEmpty()) {
            specific = cfg.getString("exploitfix." + pluginKey + ".log-channel");
        }

        // 3) Fallback
        String fallback = cfg.getString("anticheat.default-log-channel");
        if (fallback == null || fallback.isEmpty()) {
            fallback = cfg.getString("exploitfix.default-log-channel");
        }

        String channelId = (specific != null && !specific.isEmpty()) ? specific : fallback;
        if (channelId == null || channelId.isEmpty()) return;

        sendToChannel(channelId, title, description, Color.ORANGE);
    }


    /**
     * ÚJ RENDSZER — kategória alapú logolás.
     * Példa:
     *   sendCategory("discord.discordmoderation.log-channel", "Ban", "Player: X");
     *   sendCategory("moderation.litebans.log-channel", "Ban", "Player: X");
     */
    public static void sendCategory(String categoryPath, String title, String description) {
        if (!DiscordManager.ready) return;

        // 1) Csatorna ID configból
        String channelId = OmniConfig.getString(categoryPath);

        // 2) Ha nincs → fallback a fő log csatornára
        if (channelId.isEmpty()) {
            channelId = OmniConfig.getString("discord.channels.log");
        }

        // 3) Ha még mindig nincs → kilép
        if (channelId.isEmpty()) return;

        sendToChannel(channelId, title, description, Color.CYAN);
    }


    // --------------------------------------------------------------------
    // ÚJ METÓDUSOK — Egyszerű logolás csatorna ID alapján
    // --------------------------------------------------------------------

    /**
     * Sima szöveg küldése egy csatornába.
     */
    public static void send(String channelId, String message) {
        if (!DiscordManager.ready) return;
        if (channelId == null || channelId.isEmpty()) return;
        if (message == null || message.isEmpty()) return;

        var jda = DiscordManager.getInstance().getJDA();
        if (jda == null) return;

        TextChannel channel = jda.getTextChannelById(channelId);
        if (channel == null) return;

        channel.sendMessage(message).queue();
    }

    /**
     * Embed küldése csatorna ID alapján.
     */
    public static void sendEmbed(String channelId, EmbedBuilder embed) {
        if (!DiscordManager.ready) return;
        if (channelId == null || channelId.isEmpty()) return;
        if (embed == null) return;

        var jda = DiscordManager.getInstance().getJDA();
        if (jda == null) return;

        TextChannel channel = jda.getTextChannelById(channelId);
        if (channel == null) return;

        channel.sendMessageEmbeds(embed.build()).queue();
    }


    // --------------------------------------------------------------------
    // Közös metódus embed küldésére (pluginKey / category logoláshoz)
    // --------------------------------------------------------------------
    private static void sendToChannel(String channelId, String title, String description, Color color) {
        var jda = DiscordManager.getInstance().getJDA();
        if (jda == null) return;

        TextChannel channel = jda.getTextChannelById(channelId);
        if (channel == null) return;

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(title)
                .setDescription(description)
                .setColor(color)
                .setTimestamp(java.time.Instant.now());

        channel.sendMessageEmbeds(embed.build()).queue();
    }
}
