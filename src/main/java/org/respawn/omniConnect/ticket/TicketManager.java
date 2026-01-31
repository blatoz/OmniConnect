package org.respawn.omniConnect.ticket;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.bukkit.entity.Player;
import org.respawn.omniConnect.lang.LangManager;

import java.awt.*;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

public class TicketManager {

    private static TicketManager instance;

    public static void init() {
        instance = new TicketManager();
    }

    public static TicketManager getInstance() {
        return instance;
    }

    private Guild getGuild(JDA jda) {
        return jda.getGuildById(TicketConfig.getGuildId());
    }

    public TextChannel getPanelChannel(JDA jda, TicketType type) {
        Guild guild = getGuild(jda);
        return guild != null ? guild.getTextChannelById(TicketConfig.getPanelChannel(type)) : null;
    }

    private Category getCategory(JDA jda, TicketType type) {
        Guild guild = getGuild(jda);
        return guild != null ? guild.getCategoryById(TicketConfig.getCategory(type)) : null;
    }

    private Role getStaffRole(JDA jda, TicketType type) {
        Guild guild = getGuild(jda);
        return guild != null ? guild.getRoleById(TicketConfig.getStaffRole(type)) : null;
    }

    private TextChannel getLogChannel(JDA jda, TicketType type) {
        Guild guild = getGuild(jda);
        return guild != null ? guild.getTextChannelById(TicketConfig.getLogChannel(type)) : null;
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }



    // PANEL ONE TYPES
    public void sendTicketPanel(JDA jda, TicketType type) {
        TextChannel channel = getPanelChannel(jda, type);
        if (channel == null) return;

        String lang = lang();

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(LangManager.get(lang, "discord.ticket.panel.title"))
                .setDescription(LangManager.get(lang, "discord.ticket.panel.description"))
                .setColor(Color.GREEN);

        Button btn = Button.primary(type.getCreateButtonId(), type.getButtonLabel());

        channel.sendMessageEmbeds(embed.build())
                .setActionRow(btn)
                .queue();
    }

    // TICKET LÉTREHOZÁS
    public void createTicketChannel(JDA jda, Member member, TicketType type) {
        Category category = getCategory(jda, type);
        Role staffRole = getStaffRole(jda, type);

        if (category == null || staffRole == null || member == null) return;

        String lang = lang();

        String baseName = member.getUser().getName().toLowerCase().replace(" ", "-");
        String channelName = TicketConfig.getTicketNameFormat(type)
                .replace("%player%", baseName);

        category.createTextChannel(channelName).queue(channel -> {

            String topic = LangManager.get(lang, "discord.ticket.open.topic_prefix")
                    .replace("%type%", type.name())
                    .replace("%user%", member.getUser().getAsTag());

            channel.getManager().setTopic(topic).queue();

            Guild guild = category.getGuild();

            channel.upsertPermissionOverride(guild.getPublicRole())
                    .deny(Permission.VIEW_CHANNEL)
                    .queue();

            channel.upsertPermissionOverride(member)
                    .grant(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY)
                    .queue();

            channel.upsertPermissionOverride(staffRole)
                    .grant(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY, Permission.MESSAGE_MANAGE)
                    .queue();

            String title = LangManager.get(lang, "discord.ticket.open.title")
                    .replace("%type_label%", type.getButtonLabel());

            String desc = LangManager.get(lang, "discord.ticket.open.description.header")
                    .replace("%user_mention%", member.getAsMention())
                    + "\n\n"
                    + LangManager.get(lang, "discord.ticket.open.description.body")
                    .replace("%type_description%", type.getDescription())
                    + "\n\n"
                    + LangManager.get(lang, "discord.ticket.open.description.footer");

            EmbedBuilder openEmbed = new EmbedBuilder()
                    .setTitle(title)
                    .setDescription(desc)
                    .setColor(Color.CYAN);

            String closeLabel = LangManager.get(lang, "discord.ticket.open.button_close");

            channel.sendMessageEmbeds(openEmbed.build())
                    .setActionRow(Button.danger("ticket:close", closeLabel))
                    .queue();

            TextChannel log = getLogChannel(jda, type);
            if (log != null) {
                EmbedBuilder logEmbed = new EmbedBuilder()
                        .setTitle(LangManager.get(lang, "discord.ticket.log.open.title"))
                        .setColor(Color.GREEN)
                        .addField(LangManager.get(lang, "discord.ticket.log.open.type"), type.name(), true)
                        .addField(LangManager.get(lang, "discord.ticket.log.open.user"), member.getUser().getAsTag(), true)
                        .addField(LangManager.get(lang, "discord.ticket.log.open.channel"), channel.getAsMention(), false)
                        .addField(LangManager.get(lang, "discord.ticket.log.open.channel_id"), channel.getId(), true);

                log.sendMessageEmbeds(logEmbed.build()).queue();
            }
        });
    }

    // TICKET LEZÁRÁS + TRANSCRIPT
    public void closeTicketChannel(TextChannel channel, Member closer) {
        if (channel == null) return;

        String lang = lang();

        String closerName = closer != null
                ? closer.getUser().getAsTag()
                : LangManager.get(lang, "discord.ticket.close.unknown_closer");

        // Típus felismerése csatornanévből
        TicketType type = TicketTypeResolver.resolve(channel.getName());

        // Transcript, ha engedélyezve
        if (type != null && TicketConfig.isTranscriptEnabled(type)) {
            TranscriptGenerator.generateAndUpload(channel, type);
        }

        String desc = LangManager.get(lang, "discord.ticket.close.description")
                .replace("%closer%", closerName);

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(LangManager.get(lang, "discord.ticket.close.title"))
                .setDescription(desc)
                .setColor(Color.ORANGE);

        channel.sendMessageEmbeds(embed.build()).queue();

        channel.delete().queueAfter(5, TimeUnit.SECONDS);
    }
}
