package org.respawn.omniConnect.ticket;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.respawn.omniConnect.lang.LangManager;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TranscriptGenerator {

    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void generateAndUpload(TextChannel channel, TicketType type) {
        // 1) Üzenetek lekérése (max 5000)
        channel.getIterableHistory().takeAsync(5000).thenAccept(messages -> {

            // 2) Transcript formátum
            String format = TicketConfig.getTranscriptFormat(type).toLowerCase();
            String content = format.equals("txt")
                    ? generateTxt(messages, channel)
                    : generateHtml(messages, channel);

            // 3) Fájlnév
            String fileName = "transcript-" + channel.getId() + "." + format;

            // 4) Transcript csatorna ID configból
            String transcriptChannelId = TicketConfig.getTranscriptChannel(type);
            TextChannel transcriptChannel = channel.getJDA()
                    .getTextChannelById(transcriptChannelId);

            if (transcriptChannel == null) {
                System.out.println("[OmniConnect] Transcript channel not set or not found for type: " + type.name());
                return;
            }

            // 5) Fájl feltöltése (JDA 4 stílus: sendFile)
            ByteArrayInputStream stream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

            transcriptChannel.sendFile(stream, fileName).queue(success -> {
                // 6) Embed visszajelzés
                String lang = LangManager.getDefaultLanguage();

                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(LangManager.get(lang, "discord.ticket.transcript.title"))
                        .setDescription(LangManager.get(lang, "discord.ticket.transcript.description"))
                        .setColor(Color.YELLOW)
                        .addField("Ticket Channel", channel.getName(), true)
                        .addField("Ticket ID", channel.getId(), true);

                transcriptChannel.sendMessageEmbeds(embed.build()).queue();
            });
        });
    }

    // TXT transcript
    private static String generateTxt(List<Message> messages, TextChannel channel) {
        StringBuilder sb = new StringBuilder();

        sb.append("Transcript for channel: ").append(channel.getName()).append("\n");
        sb.append("Channel ID: ").append(channel.getId()).append("\n");
        sb.append("Generated at: ").append(LocalDateTime.now().format(TIME)).append("\n");
        sb.append("──────────────────────────────────────────────\n\n");

        messages.stream()
                .sorted((a, b) -> a.getTimeCreated().compareTo(b.getTimeCreated()))
                .forEach(msg -> {
                    String time = msg.getTimeCreated().toLocalDateTime().format(TIME);
                    String author = msg.getAuthor().getAsTag();
                    String content = msg.getContentDisplay();

                    sb.append("[").append(time).append("] ")
                            .append(author).append(": ")
                            .append(content).append("\n");
                });

        return sb.toString();
    }

    // HTML transcript
    private static String generateHtml(List<Message> messages, TextChannel channel) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><head><meta charset='UTF-8'><style>");
        sb.append("body { font-family: Arial; background: #1e1e1e; color: #ddd; padding: 20px; }");
        sb.append(".msg { margin-bottom: 12px; }");
        sb.append(".author { color: #4ea1ff; font-weight: bold; }");
        sb.append(".time { color: #888; font-size: 12px; }");
        sb.append("</style></head><body>");

        sb.append("<h2>Transcript – ").append(channel.getName()).append("</h2>");
        sb.append("<p>Channel ID: ").append(channel.getId()).append("</p>");
        sb.append("<p>Generated at: ").append(LocalDateTime.now().format(TIME)).append("</p>");
        sb.append("<hr>");

        messages.stream()
                .sorted((a, b) -> a.getTimeCreated().compareTo(b.getTimeCreated()))
                .forEach(msg -> {
                    String time = msg.getTimeCreated().toLocalDateTime().format(TIME);
                    String author = msg.getAuthor().getAsTag();
                    String content = msg.getContentDisplay()
                            .replace("<", "&lt;")
                            .replace(">", "&gt;");

                    sb.append("<div class='msg'>")
                            .append("<span class='author'>").append(author).append("</span>")
                            .append(" <span class='time'>[").append(time).append("]</span>")
                            .append("<br>")
                            .append(content)
                            .append("</div>");
                });

        sb.append("</body></html>");

        return sb.toString();
    }
}
