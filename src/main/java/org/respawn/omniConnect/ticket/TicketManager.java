package org.respawn.omniConnect.ticket;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.FileUpload;
import org.respawn.omniConnect.LogManager;
import org.respawn.omniConnect.Main;


import java.io.File;
import java.awt.*;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

/**
 * Jegy (ticket) kezelő - kezeli a ticket csatornák létrehozását és bezárását.
 */
public class TicketManager {

    private static TicketManager instance;

    private final String guildId;
    private final String ticketCategoryId;
    private final String supportRoleId;
    private final String logChannelId;
    private final String panelChannelId;

    /**
     * TicketManager konstruktor.
     *
     * @param guildId A guild ID
     * @param ticketCategoryId A ticket csatorna kategória ID
     * @param logChannelId A log csatorna ID
     * @param panelChannelId A panel csatorna ID
     */
    private TicketManager(String guildId,
                          String ticketCategoryId,
                          String supportRoleId,
                          String logChannelId,
                          String panelChannelId) {
        this.guildId = guildId;
        this.ticketCategoryId = ticketCategoryId;
        this.supportRoleId = supportRoleId;
        this.logChannelId = logChannelId;
        this.panelChannelId = panelChannelId;
    }

    /**
     * TicketManager inicializálása.
     *
     * @param guildId A guild ID
     * @param ticketCategoryId A ticket csatorna kategória ID

     * @param logChannelId A log csatorna ID
     * @param panelChannelId A panel csatorna ID
     */
    public static void init(String guildId,
                            String ticketCategoryId,
                            String supportRoleId,
                            String logChannelId,
                            String panelChannelId) {
        instance = new TicketManager(guildId, ticketCategoryId, supportRoleId, logChannelId, panelChannelId);
    }

    /**
     * Singleton getInstance metódus.
     *
     * @return TicketManager instancia
     */
    public static TicketManager getInstance() {
        return instance;
    }

    /**
     * A Guild objektumának lekérése az ID alapján.
     *
     * @param jda A JDA instancia
     * @return A Guild vagy null, ha nem létezik
     */
    private Guild getGuild(JDA jda) {
        return jda.getGuildById(guildId);
    }

    /**
     * A ticket kategória objektumának lekérése az ID alapján.
     *
     * @param jda A JDA instancia
     * @return A Category vagy null, ha nem létezik
     */
    private Category getTicketCategory(JDA jda) {
        Guild guild = getGuild(jda);
        return guild != null ? guild.getCategoryById(ticketCategoryId) : null;
    }

    /**
     * A support szerepkör objektumának lekérése az ID alapján.
     *
     * @param jda A JDA instancia
     * @return A Role vagy null, ha nem létezik
     */
    private Role getSupportRole(JDA jda) {
        Guild guild = getGuild(jda);
        return guild != null ? guild.getRoleById(supportRoleId) : null;
    }

    /**
     * A log csatorna objektumának lekérése az ID alapján.
     *
     * @param jda A JDA instancia
     * @return A TextChannel vagy null, ha nem létezik
     */
    private TextChannel getLogChannel(JDA jda) {
        Guild guild = getGuild(jda);
        return guild != null ? guild.getTextChannelById(logChannelId) : null;
    }

    /**
     * A panel csatorna objektumának lekérése az ID alapján.
     *
     * @param jda A JDA instancia
     * @return A TextChannel vagy null, ha nem létezik
     */
    private TextChannel getPanelChannel(JDA jda) {
        Guild guild = getGuild(jda);
        return guild != null ? guild.getTextChannelById(panelChannelId) : null;
    }

    /**
     * A ticket panel üzenetének küldése a panel csatornára.
     * Gombokat tartalmaz a különböző ticket típusokhoz.
     *
     * @param jda A JDA instancia
     */
    public void sendTicketPanel(JDA jda) {
        TextChannel channel = getPanelChannel(jda);
        if (channel == null) {
            return;
        }

        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("Support Ticket Rendszer")
                .setDescription("Válaszd ki, milyen típusú ticketet szeretnél nyitni az alábbi gombok közül.")
                .setColor(Color.GREEN);

        // 7 gomb (2 sor)
        Button supportBtn = Button.primary(TicketType.SUPPORT.getCreateButtonId(), TicketType.SUPPORT.getButtonLabel());
        Button reportBtn = Button.danger(TicketType.REPORT.getCreateButtonId(), TicketType.REPORT.getButtonLabel());
        Button bugBtn = Button.primary(TicketType.BUG.getCreateButtonId(), TicketType.BUG.getButtonLabel());
        Button tgfBtn = Button.primary(TicketType.TGF.getCreateButtonId(), TicketType.TGF.getButtonLabel());
        Button partnerBtn = Button.primary(TicketType.PARTNER.getCreateButtonId(), TicketType.PARTNER.getButtonLabel());
        Button rewardBtn = Button.success(TicketType.REWARD.getCreateButtonId(), TicketType.REWARD.getButtonLabel());
        Button lostBtn = Button.secondary(TicketType.LOST.getCreateButtonId(), TicketType.LOST.getButtonLabel());

        channel.sendMessageEmbeds(builder.build())
                .setActionRows(
                        ActionRow.of(supportBtn, reportBtn, bugBtn, tgfBtn, partnerBtn),
                        ActionRow.of(rewardBtn, lostBtn)
                )
                .queue();
    }

    /**
     * Ticket csatorna létrehozása.
     *
     * @param jda A JDA instancia
     * @param member A csatornát nyitó tag
     * @param type A ticket típusa
     */
    public void createTicketChannel(JDA jda, Member member, TicketType type) {
        Category category = getTicketCategory(jda);
        Role supportRole = getSupportRole(jda);
        if (category == null || supportRole == null || member == null) {
            return;
        }

        String baseName = member.getUser().getName().toLowerCase().replace(" ", "-");
        String channelName = type.getChannelPrefix() + "-" + baseName;

        category.createTextChannel(channelName).queue(channel -> {

            channel.getManager().setTopic(
                    "Ticket típusa: " + type.name() +
                            " | Nyitotta: " + member.getUser().getAsTag() +
                            " | UserID: " + member.getId()
            ).queue();

            Guild guild = category.getGuild();

            channel.upsertPermissionOverride(guild.getPublicRole())
                    .deny(EnumSet.of(
                            Permission.VIEW_CHANNEL,
                            Permission.MESSAGE_SEND
                    ))
                    .queue();

            channel.upsertPermissionOverride(member)
                    .grant(EnumSet.of(
                            Permission.VIEW_CHANNEL,
                            Permission.MESSAGE_SEND,
                            Permission.MESSAGE_HISTORY
                    ))
                    .queue();

            channel.upsertPermissionOverride(supportRole)
                    .grant(EnumSet.of(
                            Permission.VIEW_CHANNEL,
                            Permission.MESSAGE_SEND,
                            Permission.MESSAGE_HISTORY,
                            Permission.MESSAGE_MANAGE
                    ))
                    .queue();

            EmbedBuilder openEmbed = new EmbedBuilder()
                    .setTitle("Ticket megnyitva – " + type.getButtonLabel())
                    .setDescription(
                            "Üdv, " + member.getAsMention() + "!\n\n" +
                                    type.getDescription() + "\n\n" +
                                    "Kérjük, részletesen írd le, miben tudunk segíteni."
                    )
                    .setColor(Color.CYAN);

            channel.sendMessageEmbeds(openEmbed.build())
                    .setActionRow(
                            Button.danger("ticket:close", "🔒 Ticket lezárása")
                    )
                    .queue();

            TextChannel logChannel = getLogChannel(jda);
            if (logChannel != null) {
                EmbedBuilder log = new EmbedBuilder()
                        .setTitle("Ticket nyitva")
                        .setColor(Color.GREEN)
                        .addField("Típus", type.name(), true)
                        .addField("Felhasználó", member.getUser().getAsTag(), true)
                        .addField("Csatorna", channel.getAsMention(), false)
                        .addField("Csatorna ID", channel.getId(), true);

                logChannel.sendMessageEmbeds(log.build()).queue();
            }

            LogManager.getInstance().sendEmbed(builder ->
                    builder.setTitle("Ticket nyitva (Discord)")
                            .setColor(Color.GREEN)
                            .addField("Típus", type.name(), true)
                            .addField("Felhasználó", member.getUser().getAsTag(), true)
                            .addField("Csatorna", channel.getName(), true)
            );
        });
    }

    /**
     * Ticket csatorna lezárása és törlése.
     *
     * @param channel Az lezárandó ticket csatorna
     * @param closer A csatornát lezáró tag
     */
    public void closeTicketChannel(TextChannel channel, Member closer) {
        if (channel == null) return;

        // --- Lezárási üzenet ---
        EmbedBuilder closing = new EmbedBuilder()
                .setTitle("Ticket lezárása")
                .setDescription("A ticketet lezárta: " +
                        (closer != null ? closer.getUser().getAsTag() : "Ismeretlen"))
                .setColor(Color.ORANGE);

        channel.sendMessageEmbeds(closing.build()).queue();

        Guild guild = channel.getGuild();
        JDA jda = guild.getJDA();
        TextChannel logChannel = getLogChannel(jda);

        // --- Ticket megnyitó ID kinyerése a topic-ból ---
        String openerUserId = extractUserIdFromTopic(channel.getTopic());

        // --- Log üzenet ---
        if (logChannel != null) {
            EmbedBuilder log = new EmbedBuilder()
                    .setTitle("Ticket lezárva")
                    .setColor(Color.RED)
                    .addField("Csatorna", channel.getName(), true)
                    .addField("Csatorna ID", channel.getId(), true)
                    .addField("Lezárta", closer != null ? closer.getUser().getAsTag() : "Ismeretlen", false);
            logChannel.sendMessageEmbeds(log.build()).queue();
        }

        // --- Transcript generálás ---
        channel.getHistory().retrievePast(1000).queue(messages -> {

            // TXT transcript
            StringBuilder txt = new StringBuilder();
            txt.append("Transcript for ").append(channel.getName()).append("\n\n");

            messages.stream()
                    .sorted(Comparator.comparing(m -> m.getTimeCreated()))
                    .forEach(m -> txt.append("[")
                            .append(m.getTimeCreated())
                            .append("] ")
                            .append(m.getAuthor().getName())
                            .append(": ")
                            .append(m.getContentDisplay())
                            .append("\n"));

            // HTML transcript
            StringBuilder html = new StringBuilder();
            html.append("<html><body style='font-family:Arial;'>");
            html.append("<h2>Transcript: ").append(channel.getName()).append("</h2>");

            messages.stream()
                    .sorted(Comparator.comparing(m -> m.getTimeCreated()))
                    .forEach(m -> html.append("<p><b>")
                            .append(m.getAuthor().getName())
                            .append("</b>: ")
                            .append(m.getContentDisplay())
                            .append("</p>"));

            html.append("</body></html>");

            // Mentés fájlba
            File folder = new File(Main.getInstance().getDataFolder(), "transcripts");
            if (!folder.exists()) folder.mkdirs();

            File txtFile = new File(folder, channel.getName() + ".txt");
            File htmlFile = new File(folder, channel.getName() + ".html");

            try {
                Files.writeString(txtFile.toPath(), txt.toString());
                Files.writeString(htmlFile.toPath(), html.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Küldés log csatornába
            if (logChannel != null) {
                logChannel.sendMessage("📄 **Transcript a(z) " + channel.getName() + " tickethez:**")
                        .addFile(txtFile)
                        .addFile(htmlFile)
                        .queue();
            }

            // Küldés a ticket megnyitónak DM-ben
            if (openerUserId != null && !openerUserId.isEmpty()) {
                jda.retrieveUserById(openerUserId).queue(user -> {
                    if (user != null) {
                        EmbedBuilder dmEmbed = new EmbedBuilder()
                                .setTitle("Ticket lezárva")
                                .setDescription("A(z) " + channel.getName() + " ticketed lezárásra került.")
                                .addField("Lezárta", closer != null ? closer.getUser().getAsTag() : "Ismeretlen", false)
                                .setColor(Color.ORANGE)
                                .setTimestamp(Instant.now());

                        user.openPrivateChannel().queue(privateChannel -> {
                            privateChannel.sendMessageEmbeds(dmEmbed.build())
                                    .addFile(txtFile)
                                    .addFile(htmlFile)
                                    .queue();
                        });
                    }
                });
            }

            // Csatorna törlése
            channel.delete().queueAfter(5, TimeUnit.SECONDS);
        });
    }

    /**
     * Felhasználó ID kinyerése a csatorna topic-ból.
     * A topic formátuma: "Ticket típusa: ... | Nyitotta: ... | UserID: 123456"
     *
     * @param topic A csatorna topic stringje
     * @return A felhasználó ID vagy null, ha nem található
     */
    private String extractUserIdFromTopic(String topic) {
        if (topic == null || !topic.contains("UserID:")) {
            return null;
        }

        int startIndex = topic.indexOf("UserID:") + "UserID:".length();
        String userIdPart = topic.substring(startIndex).trim();
        String userId = userIdPart.split(" ")[0];

        return userId.isEmpty() ? null : userId;
    }
}