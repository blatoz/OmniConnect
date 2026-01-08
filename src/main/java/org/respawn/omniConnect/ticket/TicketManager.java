package org.respawn.omniConnect.ticket;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.respawn.omniConnect.LogManager;

import java.awt.*;
import java.util.EnumSet;

/**
 * Jegy (ticket) kezelő - kezeli a ticket csatornák létrehozását és bezárását.
 */
public class TicketManager {

    private static TicketManager instance;

    private final String guildId;
    private final String ticketCategoryId;
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
                          String logChannelId,
                          String panelChannelId) {
        this.guildId = guildId;
        this.ticketCategoryId = ticketCategoryId;
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
                            String logChannelId,
                            String panelChannelId) {
        instance = new TicketManager(guildId, ticketCategoryId, logChannelId, panelChannelId);
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
        if (guild == null) return null;


        String roleId = TicketConfig.getInstance().getStaffRoleId();
        return guild.getRoleById(roleId);
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
                            " | Nyitotta: " + member.getUser().getAsTag()
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
                    .setTitle("Ticket Megnyitva – " + type.getButtonLabel())
                    .setDescription(
                            "Üdv, " + member.getAsMention() + "!\n\n" +
                                    type.getDescription() + "\n\n" +
                                    "Kérjük, részletesen írd le, miben tudunk segíteni."
                    )
                    .setColor(Color.CYAN);

            channel.sendMessageEmbeds(openEmbed.build())
                    .setActionRow(
                            Button.danger("ticket:close", "🔒 Ticket Lezárása")
                    )
                    .queue();

            TextChannel logChannel = getLogChannel(jda);
            if (logChannel != null) {
                EmbedBuilder log = new EmbedBuilder()
                        .setTitle("Ticket Nyitva")
                        .setColor(Color.GREEN)
                        .addField("Típus", type.name(), true)
                        .addField("Felhasználó", member.getUser().getAsTag(), true)
                        .addField("Csatorna", channel.getAsMention(), false)
                        .addField("Csatorna ID", channel.getId(), true);

                logChannel.sendMessageEmbeds(log.build()).queue();
            }

            LogManager.getInstance().sendEmbed(builder ->
                    builder.setTitle("Ticket Nyitva (Discord)")
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
        if (channel == null) {
            return;
        }

        EmbedBuilder closing = new EmbedBuilder()
                .setTitle("Ticket Lezárása")
                .setDescription("A ticketet lezárta: " +
                        (closer != null ? closer.getUser().getAsTag() : "Ismeretlen"))
                .setColor(Color.ORANGE);

        channel.sendMessageEmbeds(closing.build()).queue();

        Guild guild = channel.getGuild();
        TextChannel logChannel = getLogChannel(guild.getJDA());
        if (logChannel != null) {
            EmbedBuilder log = new EmbedBuilder()
                    .setTitle("Ticket Lezárva")
                    .setColor(Color.RED)
                    .addField("Csatorna", channel.getName(), true)
                    .addField("Csatorna ID", channel.getId(), true)
                    .addField("Lezárta", closer != null ? closer.getUser().getAsTag() : "Ismeretlen", false);
            logChannel.sendMessageEmbeds(log.build()).queue();
        }

        LogManager.getInstance().sendEmbed(builder ->
                builder.setTitle("Ticket Lezárva (Discord)")
                        .setColor(Color.RED)
                        .addField("Csatorna", channel.getName(), true)
                        .addField("Lezárta", closer != null ? closer.getUser().getAsTag() : "Ismeretlen", false)
        );

        channel.delete().queueAfter(5, java.util.concurrent.TimeUnit.SECONDS);
    }
}