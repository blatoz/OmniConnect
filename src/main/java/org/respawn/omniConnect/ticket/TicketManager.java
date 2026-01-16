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
import org.respawn.omniConnect.lang.LangManager;

import java.awt.*;
import java.util.EnumSet;

public class TicketManager {

    private static TicketManager instance;

    private final String guildId;
    private final String ticketCategoryId;
    private final String logChannelId;
    private final String panelChannelId;

    private TicketManager(String guildId,
                          String ticketCategoryId,
                          String logChannelId,
                          String panelChannelId) {
        this.guildId = guildId;
        this.ticketCategoryId = ticketCategoryId;
        this.logChannelId = logChannelId;
        this.panelChannelId = panelChannelId;
    }

    public static void init(String guildId,
                            String ticketCategoryId,
                            String logChannelId,
                            String panelChannelId) {
        instance = new TicketManager(guildId, ticketCategoryId, logChannelId, panelChannelId);
    }

    public static TicketManager getInstance() {
        return instance;
    }

    private Guild getGuild(JDA jda) {
        return jda.getGuildById(guildId);
    }

    private Category getTicketCategory(JDA jda) {
        Guild guild = getGuild(jda);
        return guild != null ? guild.getCategoryById(ticketCategoryId) : null;
    }

    private Role getSupportRole(JDA jda) {
        Guild guild = getGuild(jda);
        if (guild == null) return null;

        String roleId = TicketConfig.getInstance().getStaffRoleId();
        return guild.getRoleById(roleId);
    }

    private TextChannel getLogChannel(JDA jda) {
        Guild guild = getGuild(jda);
        return guild != null ? guild.getTextChannelById(logChannelId) : null;
    }

    private TextChannel getPanelChannel(JDA jda) {
        Guild guild = getGuild(jda);
        return guild != null ? guild.getTextChannelById(panelChannelId) : null;
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    public void sendTicketPanel(JDA jda) {
        TextChannel channel = getPanelChannel(jda);
        if (channel == null) return;

        String lang = lang();

        EmbedBuilder builder = new EmbedBuilder()
                .setTitle(LangManager.get(lang, "discord.ticket.panel.title"))
                .setDescription(LangManager.get(lang, "discord.ticket.panel.description"))
                .setColor(Color.GREEN);

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

    public void createTicketChannel(JDA jda, Member member, TicketType type) {
        Category category = getTicketCategory(jda);
        Role supportRole = getSupportRole(jda);
        if (category == null || supportRole == null || member == null) return;

        String lang = lang();

        String baseName = member.getUser().getName().toLowerCase().replace(" ", "-");
        String channelName = type.getChannelPrefix() + "-" + baseName;

        category.createTextChannel(channelName).queue(channel -> {

            String topicTemplate = LangManager.get(lang, "discord.ticket.open.topic_prefix");
            String topic = topicTemplate
                    .replace("%type%", type.name())
                    .replace("%user%", member.getUser().getAsTag());

            channel.getManager().setTopic(topic).queue();

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

            String titleTemplate = LangManager.get(lang, "discord.ticket.open.title");
            String header = LangManager.get(lang, "discord.ticket.open.description.header");
            String body = LangManager.get(lang, "discord.ticket.open.description.body");
            String footer = LangManager.get(lang, "discord.ticket.open.description.footer");

            String title = titleTemplate.replace("%type_label%", type.getButtonLabel());
            String desc = header.replace("%user_mention%", member.getAsMention())
                    + "\n\n"
                    + body.replace("%type_description%", type.getDescription())
                    + "\n\n"
                    + footer;

            EmbedBuilder openEmbed = new EmbedBuilder()
                    .setTitle(title)
                    .setDescription(desc)
                    .setColor(Color.CYAN);

            String closeLabel = LangManager.get(lang, "discord.ticket.open.button_close");

            channel.sendMessageEmbeds(openEmbed.build())
                    .setActionRow(
                            Button.danger("ticket:close", closeLabel)
                    )
                    .queue();

            TextChannel logChannel = getLogChannel(jda);
            if (logChannel != null) {
                EmbedBuilder log = new EmbedBuilder()
                        .setTitle(LangManager.get(lang, "discord.ticket.log.open.title"))
                        .setColor(Color.GREEN)
                        .addField(LangManager.get(lang, "discord.ticket.log.open.type"), type.name(), true)
                        .addField(LangManager.get(lang, "discord.ticket.log.open.user"), member.getUser().getAsTag(), true)
                        .addField(LangManager.get(lang, "discord.ticket.log.open.channel"), channel.getAsMention(), false)
                        .addField(LangManager.get(lang, "discord.ticket.log.open.channel_id"), channel.getId(), true);

                logChannel.sendMessageEmbeds(log.build()).queue();
            }

            LogManager.getInstance().sendEmbed(builder ->
                    builder.setTitle(LangManager.get(lang, "discord.ticket.log.internal_open.title"))
                            .setColor(Color.GREEN)
                            .addField(LangManager.get(lang, "discord.ticket.log.internal_open.type"), type.name(), true)
                            .addField(LangManager.get(lang, "discord.ticket.log.internal_open.user"), member.getUser().getAsTag(), true)
                            .addField(LangManager.get(lang, "discord.ticket.log.internal_open.user_id"), member.getId(), true)
                            .addField(LangManager.get(lang, "discord.ticket.log.internal_open.channel"), channel.getName(), true)
            );
        });
    }

    public void closeTicketChannel(TextChannel channel, Member closer) {
        if (channel == null) return;

        String lang = lang();

        String closerName = closer != null
                ? closer.getUser().getAsTag()
                : LangManager.get(lang, "discord.ticket.close.unknown_closer");

        String closingDescTemplate = LangManager.get(lang, "discord.ticket.close.description");
        String closingDesc = closingDescTemplate.replace("%closer%", closerName);

        EmbedBuilder closing = new EmbedBuilder()
                .setTitle(LangManager.get(lang, "discord.ticket.close.title"))
                .setDescription(closingDesc)
                .setColor(Color.ORANGE);

        channel.sendMessageEmbeds(closing.build()).queue();

        Guild guild = channel.getGuild();
        TextChannel logChannel = getLogChannel(guild.getJDA());
        if (logChannel != null) {
            EmbedBuilder log = new EmbedBuilder()
                    .setTitle(LangManager.get(lang, "discord.ticket.log.close.title"))
                    .setColor(Color.RED)
                    .addField(LangManager.get(lang, "discord.ticket.log.close.channel"), channel.getName(), true)
                    .addField(LangManager.get(lang, "discord.ticket.log.close.channel_id"), channel.getId(), true)
                    .addField(LangManager.get(lang, "discord.ticket.log.close.closed_by"), closerName, false);
            logChannel.sendMessageEmbeds(log.build()).queue();
        }

        LogManager.getInstance().sendEmbed(builder ->
                builder.setTitle(LangManager.get(lang, "discord.ticket.log.internal_close.title"))
                        .setColor(Color.RED)
                        .addField(LangManager.get(lang, "discord.ticket.log.internal_close.channel"), channel.getName(), true)
                        .addField(LangManager.get(lang, "discord.ticket.log.internal_close.channel_id"), channel.getId(), true)
                        .addField(LangManager.get(lang, "discord.ticket.log.internal_close.closed_by"), closerName, false)
        );

        channel.delete().queueAfter(5, java.util.concurrent.TimeUnit.SECONDS);
    }
}
