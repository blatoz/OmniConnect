package org.respawn.omniConnect.hooks;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.respawn.omniConnect.Main;

import java.awt.*;

public class DiscordLog {

    private static JDA jda;

    public static void init(String token) {
        try {
            jda = JDABuilder.createDefault(token).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shutdown() {
        if (jda != null) {
            jda.shutdown();
        }
    }

    public static void send(String pluginKey, String title, String description) {
        if (jda == null) return;

        var cfg = Main.getInstance().getConfig();

        // Anticheat vagy exploitfix?
        String specific = cfg.getString("anticheat." + pluginKey + ".log-channel");
        if (specific == null) {
            specific = cfg.getString("exploitfix." + pluginKey + ".log-channel");
        }

        String fallback = cfg.getString("anticheat.default-log-channel");
        if (fallback == null || fallback.isEmpty()) {
            fallback = cfg.getString("exploitfix.default-log-channel");
        }

        String channelId = (specific != null && !specific.isEmpty()) ? specific : fallback;
        if (channelId == null || channelId.isEmpty()) return;

        TextChannel channel = jda.getTextChannelById(channelId);
        if (channel == null) return;

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(title)
                .setDescription(description)
                .setColor(Color.ORANGE)
                .setTimestamp(java.time.Instant.now());

        channel.sendMessageEmbeds(embed.build()).queue();
    }


    private static String getChannelFor(String pluginKey) {
        var cfg = Main.getInstance().getConfig();

        String specific = cfg.getString("anticheat." + pluginKey + ".log-channel");
        String fallback = cfg.getString("anticheat.default-log-channel");

        if (specific != null && !specific.isEmpty()) {
            return specific;
        }
        return fallback;
    }
}
