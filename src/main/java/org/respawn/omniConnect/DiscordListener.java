package org.respawn.omniConnect;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.guild.voice.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberUpdateEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateAvatarEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateDiscriminatorEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;

import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;

import net.dv8tion.jda.api.events.channel.update.ChannelUpdateNameEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateTopicEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateNSFWEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateBitrateEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateUserLimitEvent;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import org.jetbrains.annotations.NotNull;
import org.respawn.omniConnect.lang.LangManager;

import java.awt.*;
import java.util.stream.Collectors;
import java.util.Optional;

/**
 * Discord event monitor - logs all server and user activity.
 * Handles events for messages, channels, roles, and voice channels.
 */
public class DiscordListener extends ListenerAdapter {

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!event.isFromGuild()) return;

        String lang = lang();

        Message msg = event.getMessage();
        MessageCache.store(msg);

        final String attachments = msg.getAttachments().isEmpty()
                ? LangManager.get(lang, "discord.log.common.no_attachments")
                : msg.getAttachments().stream()
                .map(a -> a.getFileName() + " (" + a.getUrl() + ")")
                .collect(Collectors.joining("\n"));

        String content = msg.getContentRaw().isEmpty()
                ? LangManager.get(lang, "discord.log.common.no_content")
                : msg.getContentRaw();

        LogManager.getInstance().sendEmbed(builder -> builder
                .setTitle(LangManager.get(lang, "discord.log.message.received.title"))
                .setColor(Color.CYAN)
                .addField(LangManager.get(lang, "discord.log.message.received.user"), event.getAuthor().getName(), true)
                .addField(LangManager.get(lang, "discord.log.message.received.user_id"), event.getAuthor().getId(), true)
                .addField(LangManager.get(lang, "discord.log.message.received.channel"), event.getChannel().getName(), true)
                .addField(LangManager.get(lang, "discord.log.message.received.channel_id"), event.getChannel().getId(), true)
                .addField(LangManager.get(lang, "discord.log.message.received.content"), "```\n" + content + "\n```", false)
                .addField(LangManager.get(lang, "discord.log.message.received.attachments"), attachments, false)
                .addField(LangManager.get(lang, "discord.log.message.received.message_id"), msg.getId(), true)
        );
    }

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {
        if (!event.isFromGuild()) return;

        String lang = lang();

        Message cached = MessageCache.get(event.getMessageId());

        final String contentFinal;
        final String attachmentsFinal;
        final String authorFinal;

        if (cached != null) {
            contentFinal = cached.getContentRaw().isEmpty()
                    ? LangManager.get(lang, "discord.log.common.no_content")
                    : cached.getContentRaw();

            attachmentsFinal = cached.getAttachments().isEmpty()
                    ? LangManager.get(lang, "discord.log.common.no_attachments")
                    : cached.getAttachments().stream()
                    .map(a -> a.getFileName() + " (" + a.getUrl() + ")")
                    .collect(Collectors.joining("\n"));

            authorFinal = cached.getAuthor().getName();
        } else {
            contentFinal = LangManager.get(lang, "discord.log.common.no_content");
            attachmentsFinal = LangManager.get(lang, "discord.log.common.no_attachments");
            authorFinal = LangManager.get(lang, "discord.log.common.unknown");
        }

        LogManager.getInstance().sendEmbed(builder -> builder
                .setTitle(LangManager.get(lang, "discord.log.message.delete.title"))
                .setColor(Color.ORANGE)
                .addField(LangManager.get(lang, "discord.log.message.delete.channel"), event.getChannel().getName(), true)
                .addField(LangManager.get(lang, "discord.log.message.delete.channel_id"), event.getChannel().getId(), true)
                .addField(LangManager.get(lang, "discord.log.message.delete.user"), authorFinal, true)
                .addField(LangManager.get(lang, "discord.log.message.delete.content"), "```\n" + contentFinal + "\n```", false)
                .addField(LangManager.get(lang, "discord.log.message.delete.attachments"), attachmentsFinal, false)
                .addField(LangManager.get(lang, "discord.log.message.delete.message_id"), event.getMessageId(), true)
        );

        MessageCache.remove(event.getMessageId());
    }

    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!event.isFromGuild()) return;

        String lang = lang();

        Message msg = event.getMessage();
        MessageCache.store(msg);

        final String attachments = msg.getAttachments().isEmpty()
                ? LangManager.get(lang, "discord.log.common.no_attachments")
                : msg.getAttachments().stream()
                .map(a -> a.getFileName() + " (" + a.getUrl() + ")")
                .collect(Collectors.joining("\n"));

        String content = msg.getContentRaw().isEmpty()
                ? LangManager.get(lang, "discord.log.common.no_content")
                : msg.getContentRaw();

        LogManager.getInstance().sendEmbed(builder -> builder
                .setTitle(LangManager.get(lang, "discord.log.message.update.title"))
                .setColor(Color.YELLOW)
                .addField(LangManager.get(lang, "discord.log.message.update.user"), event.getAuthor().getName(), true)
                .addField(LangManager.get(lang, "discord.log.message.update.user_id"), event.getAuthor().getId(), true)
                .addField(LangManager.get(lang, "discord.log.message.update.channel"), event.getChannel().getName(), true)
                .addField(LangManager.get(lang, "discord.log.message.update.new_content"), "```\n" + content + "\n```", false)
                .addField(LangManager.get(lang, "discord.log.message.update.attachments"), attachments, false)
                .addField(LangManager.get(lang, "discord.log.message.update.message_id"), msg.getId(), true)
        );
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        String lang = lang();

        LogManager.getInstance().sendEmbed(builder -> builder
                .setTitle(LangManager.get(lang, "discord.log.member.join.title"))
                .setColor(Color.GREEN)
                .addField(LangManager.get(lang, "discord.log.member.join.user"), event.getUser().getName(), false)
                .addField(LangManager.get(lang, "discord.log.member.join.user_id"), event.getUser().getId(), false)
                .addField(LangManager.get(lang, "discord.log.member.join.created"), event.getUser().getTimeCreated().toString(), false)
                .addField(LangManager.get(lang, "discord.log.member.join.is_bot"), event.getUser().isBot() ? "Yes" : "No", false)
        );
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        String lang = lang();

        LogManager.getInstance().sendEmbed(builder -> builder
                .setTitle(LangManager.get(lang, "discord.log.member.leave.title"))
                .setColor(Color.RED)
                .addField(LangManager.get(lang, "discord.log.member.leave.user"), event.getUser().getName(), false)
                .addField(LangManager.get(lang, "discord.log.member.leave.user_id"), event.getUser().getId(), false)
                .addField(LangManager.get(lang, "discord.log.member.leave.created"), event.getUser().getTimeCreated().toString(), false)
                .addField(LangManager.get(lang, "discord.log.member.leave.is_bot"), event.getUser().isBot() ? "Yes" : "No", false)
        );
    }

    @Override
    public void onUserUpdateName(@NotNull UserUpdateNameEvent event) {
        String lang = lang();

        final String oldName = event.getOldValue();
        final String newName = event.getNewValue();

        LogManager.getInstance().sendEmbed(builder -> builder
                .setTitle(LangManager.get(lang, "discord.log.member.name_update.title"))
                .setColor(Color.YELLOW)
                .addField(LangManager.get(lang, "discord.log.member.name_update.user_id"), event.getUser().getId(), true)
                .addField(LangManager.get(lang, "discord.log.member.name_update.old"), oldName, true)
                .addField(LangManager.get(lang, "discord.log.member.name_update.new"), newName, true)
        );
    }

    @Override
    public void onUserUpdateAvatar(@NotNull UserUpdateAvatarEvent event) {
        String lang = lang();

        final String oldAvatarUrl = event.getOldValue();
        final String newAvatarUrl = event.getNewValue();

        String oldText = oldAvatarUrl != null ? oldAvatarUrl : LangManager.get(lang, "discord.log.common.none");
        String newText = newAvatarUrl != null ? newAvatarUrl : LangManager.get(lang, "discord.log.common.none");

        LogManager.getInstance().sendEmbed(builder -> builder
                .setTitle(LangManager.get(lang, "discord.log.member.avatar_update.title"))
                .setColor(Color.CYAN)
                .addField(LangManager.get(lang, "discord.log.member.avatar_update.user"), event.getUser().getName(), true)
                .addField(LangManager.get(lang, "discord.log.member.avatar_update.user_id"), event.getUser().getId(), true)
                .addField(LangManager.get(lang, "discord.log.member.avatar_update.old"), oldText, false)
                .addField(LangManager.get(lang, "discord.log.member.avatar_update.new"), newText, false)
        );
    }

    @Override
    public void onUserUpdateDiscriminator(@NotNull UserUpdateDiscriminatorEvent event) {
        String lang = lang();

        final String oldDiscriminator = event.getOldValue();
        final String newDiscriminator = event.getNewValue();

        LogManager.getInstance().sendEmbed(builder -> builder
                .setTitle(LangManager.get(lang, "discord.log.member.discriminator_update.title"))
                .setColor(Color.LIGHT_GRAY)
                .addField(LangManager.get(lang, "discord.log.member.discriminator_update.user"), event.getUser().getName(), true)
                .addField(LangManager.get(lang, "discord.log.member.discriminator_update.user_id"), event.getUser().getId(), true)
                .addField(LangManager.get(lang, "discord.log.member.discriminator_update.old"), oldDiscriminator, true)
                .addField(LangManager.get(lang, "discord.log.member.discriminator_update.new"), newDiscriminator, true)
        );
    }

    @Override
    public void onGuildBan(@NotNull GuildBanEvent event) {
        String lang = lang();

        event.getGuild().retrieveAuditLogs()
                .type(ActionType.BAN)
                .limit(5)
                .queue(logs -> {
                    String executor = LangManager.get(lang, "discord.log.common.unknown");
                    String reason = LangManager.get(lang, "discord.log.common.no_reason");

                    Optional<AuditLogEntry> entry = logs.stream()
                            .filter(log -> log.getTargetId().equals(event.getUser().getId()))
                            .filter(log -> System.currentTimeMillis() - log.getTimeCreated().toInstant().toEpochMilli() < 5000)
                            .findFirst();

                    if (entry.isPresent()) {
                        AuditLogEntry auditEntry = entry.get();
                        if (auditEntry.getUser() != null) {
                            executor = auditEntry.getUser().getName();
                        }
                        if (auditEntry.getReason() != null) {
                            reason = auditEntry.getReason();
                        }
                    }

                    final String finalExecutor = executor;
                    final String finalReason = reason;

                    LogManager.getInstance().sendEmbed(builder -> builder
                            .setTitle(LangManager.get(lang, "discord.log.ban.title"))
                            .setColor(Color.RED)
                            .addField(LangManager.get(lang, "discord.log.ban.user"), event.getUser().getName(), true)
                            .addField(LangManager.get(lang, "discord.log.ban.user_id"), event.getUser().getId(), true)
                            .addField(LangManager.get(lang, "discord.log.ban.executor"), finalExecutor, true)
                            .addField(LangManager.get(lang, "discord.log.ban.reason"), finalReason, false)
                    );
                });
    }

    @Override
    public void onGuildUnban(@NotNull GuildUnbanEvent event) {
        String lang = lang();

        event.getGuild().retrieveAuditLogs()
                .type(ActionType.UNBAN)
                .limit(5)
                .queue(logs -> {
                    String executor = LangManager.get(lang, "discord.log.common.unknown");
                    String reason = LangManager.get(lang, "discord.log.common.no_reason");

                    Optional<AuditLogEntry> entry = logs.stream()
                            .filter(log -> log.getTargetId().equals(event.getUser().getId()))
                            .filter(log -> System.currentTimeMillis() - log.getTimeCreated().toInstant().toEpochMilli() < 5000)
                            .findFirst();

                    if (entry.isPresent()) {
                        AuditLogEntry auditEntry = entry.get();
                        if (auditEntry.getUser() != null) {
                            executor = auditEntry.getUser().getName();
                        }
                        if (auditEntry.getReason() != null) {
                            reason = auditEntry.getReason();
                        }
                    }

                    final String finalExecutor = executor;
                    final String finalReason = reason;

                    LogManager.getInstance().sendEmbed(builder -> builder
                            .setTitle(LangManager.get(lang, "discord.log.unban.title"))
                            .setColor(Color.GREEN)
                            .addField(LangManager.get(lang, "discord.log.unban.user"), event.getUser().getName(), true)
                            .addField(LangManager.get(lang, "discord.log.unban.user_id"), event.getUser().getId(), true)
                            .addField(LangManager.get(lang, "discord.log.unban.executor"), finalExecutor, true)
                            .addField(LangManager.get(lang, "discord.log.unban.reason"), finalReason, false)
                    );
                });
    }

    @Override
    public void onGuildMemberRoleAdd(@NotNull GuildMemberRoleAddEvent event) {
        String lang = lang();

        final String roles = event.getRoles().isEmpty()
                ? LangManager.get(lang, "discord.log.common.none")
                : event.getRoles().stream()
                .map(net.dv8tion.jda.api.entities.Role::getName)
                .collect(Collectors.joining(", "));

        event.getGuild().retrieveAuditLogs()
                .type(ActionType.MEMBER_ROLE_UPDATE)
                .limit(5)
                .queue(logs -> {
                    String executor = LangManager.get(lang, "discord.log.common.unknown");
                    String reason = LangManager.get(lang, "discord.log.common.no_reason");

                    Optional<AuditLogEntry> entry = logs.stream()
                            .filter(log -> log.getTargetId().equals(event.getUser().getId()))
                            .filter(log -> System.currentTimeMillis() - log.getTimeCreated().toInstant().toEpochMilli() < 5000)
                            .findFirst();

                    if (entry.isPresent()) {
                        AuditLogEntry auditEntry = entry.get();
                        if (auditEntry.getUser() != null) {
                            executor = auditEntry.getUser().getName();
                        }
                        if (auditEntry.getReason() != null) {
                            reason = auditEntry.getReason();
                        }
                    }

                    final String finalExecutor = executor;
                    final String finalReason = reason;

                    LogManager.getInstance().sendEmbed(builder -> builder
                            .setTitle(LangManager.get(lang, "discord.log.role.add.title"))
                            .setColor(Color.MAGENTA)
                            .addField(LangManager.get(lang, "discord.log.role.add.user"), event.getUser().getName(), true)
                            .addField(LangManager.get(lang, "discord.log.role.add.user_id"), event.getUser().getId(), true)
                            .addField(LangManager.get(lang, "discord.log.role.add.roles"), roles, false)
                            .addField(LangManager.get(lang, "discord.log.role.add.executor"), finalExecutor, true)
                            .addField(LangManager.get(lang, "discord.log.role.add.reason"), finalReason, false)
                    );
                });
    }

    @Override
    public void onGuildMemberRoleRemove(@NotNull GuildMemberRoleRemoveEvent event) {
        String lang = lang();

        final String roles = event.getRoles().isEmpty()
                ? LangManager.get(lang, "discord.log.common.none")
                : event.getRoles().stream()
                .map(net.dv8tion.jda.api.entities.Role::getName)
                .collect(Collectors.joining(", "));

        event.getGuild().retrieveAuditLogs()
                .type(ActionType.MEMBER_ROLE_UPDATE)
                .limit(5)
                .queue(logs -> {
                    String executor = LangManager.get(lang, "discord.log.common.unknown");
                    String reason = LangManager.get(lang, "discord.log.common.no_reason");

                    Optional<AuditLogEntry> entry = logs.stream()
                            .filter(log -> log.getTargetId().equals(event.getUser().getId()))
                            .filter(log -> System.currentTimeMillis() - log.getTimeCreated().toInstant().toEpochMilli() < 5000)
                            .findFirst();

                    if (entry.isPresent()) {
                        AuditLogEntry auditEntry = entry.get();
                        if (auditEntry.getUser() != null) {
                            executor = auditEntry.getUser().getName();
                        }
                        if (auditEntry.getReason() != null) {
                            reason = auditEntry.getReason();
                        }
                    }

                    final String finalExecutor = executor;
                    final String finalReason = reason;

                    LogManager.getInstance().sendEmbed(builder -> builder
                            .setTitle(LangManager.get(lang, "discord.log.role.remove.title"))
                            .setColor(Color.PINK)
                            .addField(LangManager.get(lang, "discord.log.role.remove.user"), event.getUser().getName(), true)
                            .addField(LangManager.get(lang, "discord.log.role.remove.user_id"), event.getUser().getId(), true)
                            .addField(LangManager.get(lang, "discord.log.role.remove.roles"), roles, false)
                            .addField(LangManager.get(lang, "discord.log.role.remove.executor"), finalExecutor, true)
                            .addField(LangManager.get(lang, "discord.log.role.remove.reason"), finalReason, false)
                    );
                });
    }

    @Override
    public void onChannelCreate(@NotNull ChannelCreateEvent event) {
        String lang = lang();

        event.getGuild().retrieveAuditLogs()
                .type(ActionType.CHANNEL_CREATE)
                .limit(5)
                .queue(logs -> {
                    String executor = LangManager.get(lang, "discord.log.common.unknown");
                    String reason = LangManager.get(lang, "discord.log.common.no_reason");

                    Optional<AuditLogEntry> entry = logs.stream()
                            .filter(log -> log.getTargetId().equals(event.getChannel().getId()))
                            .filter(log -> System.currentTimeMillis() - log.getTimeCreated().toInstant().toEpochMilli() < 5000)
                            .findFirst();

                    if (entry.isPresent()) {
                        AuditLogEntry auditEntry = entry.get();
                        if (auditEntry.getUser() != null) {
                            executor = auditEntry.getUser().getName();
                        }
                        if (auditEntry.getReason() != null) {
                            reason = auditEntry.getReason();
                        }
                    }

                    final String finalExecutor = executor;
                    final String finalReason = reason;

                    LogManager.getInstance().sendEmbed(builder -> builder
                            .setTitle(LangManager.get(lang, "discord.log.channel.create.title"))
                            .setColor(Color.BLUE)
                            .addField(LangManager.get(lang, "discord.log.channel.create.name"), event.getChannel().getName(), false)
                            .addField(LangManager.get(lang, "discord.log.channel.create.id"), event.getChannel().getId(), false)
                            .addField(LangManager.get(lang, "discord.log.channel.create.type"), event.getChannel().getType().toString(), false)
                            .addField(LangManager.get(lang, "discord.log.channel.create.executor"), finalExecutor, true)
                            .addField(LangManager.get(lang, "discord.log.channel.create.reason"), finalReason, false)
                    );
                });
    }

    @Override
    public void onChannelDelete(@NotNull ChannelDeleteEvent event) {
        String lang = lang();

        event.getGuild().retrieveAuditLogs()
                .type(ActionType.CHANNEL_DELETE)
                .limit(5)
                .queue(logs -> {
                    String executor = LangManager.get(lang, "discord.log.common.unknown");
                    String reason = LangManager.get(lang, "discord.log.common.no_reason");

                    Optional<AuditLogEntry> entry = logs.stream()
                            .filter(log -> log.getTargetId().equals(event.getChannel().getId()))
                            .filter(log -> System.currentTimeMillis() - log.getTimeCreated().toInstant().toEpochMilli() < 5000)
                            .findFirst();

                    if (entry.isPresent()) {
                        AuditLogEntry auditEntry = entry.get();
                        if (auditEntry.getUser() != null) {
                            executor = auditEntry.getUser().getName();
                        }
                        if (auditEntry.getReason() != null) {
                            reason = auditEntry.getReason();
                        }
                    }

                    final String finalExecutor = executor;
                    final String finalReason = reason;

                    LogManager.getInstance().sendEmbed(builder -> builder
                            .setTitle(LangManager.get(lang, "discord.log.channel.delete.title"))
                            .setColor(Color.RED)
                            .addField(LangManager.get(lang, "discord.log.channel.delete.name"), event.getChannel().getName(), false)
                            .addField(LangManager.get(lang, "discord.log.channel.delete.id"), event.getChannel().getId(), false)
                            .addField(LangManager.get(lang, "discord.log.channel.delete.type"), event.getChannel().getType().toString(), false)
                            .addField(LangManager.get(lang, "discord.log.channel.delete.executor"), finalExecutor, true)
                            .addField(LangManager.get(lang, "discord.log.channel.delete.reason"), finalReason, false)
                    );
                });
    }

    @Override
    public void onChannelUpdateName(@NotNull ChannelUpdateNameEvent event) {
        String lang = lang();

        final String oldName = event.getOldValue();
        final String newName = event.getNewValue();

        if (oldName != null && newName != null && !oldName.equals(newName)) {
            event.getGuild().retrieveAuditLogs()
                    .type(ActionType.CHANNEL_UPDATE)
                    .limit(5)
                    .queue(logs -> {
                        String executor = LangManager.get(lang, "discord.log.common.unknown");
                        String reason = LangManager.get(lang, "discord.log.common.no_reason");

                        Optional<AuditLogEntry> entry = logs.stream()
                                .filter(log -> log.getTargetId().equals(event.getChannel().getId()))
                                .filter(log -> System.currentTimeMillis() - log.getTimeCreated().toInstant().toEpochMilli() < 5000)
                                .findFirst();

                        if (entry.isPresent()) {
                            AuditLogEntry auditEntry = entry.get();
                            if (auditEntry.getUser() != null) {
                                executor = auditEntry.getUser().getName();
                            }
                            if (auditEntry.getReason() != null) {
                                reason = auditEntry.getReason();
                            }
                        }

                        final String finalExecutor = executor;
                        final String finalReason = reason;

                        LogManager.getInstance().sendEmbed(builder -> builder
                                .setTitle(LangManager.get(lang, "discord.log.channel.update.name.title"))
                                .setColor(Color.YELLOW)
                                .addField(LangManager.get(lang, "discord.log.channel.update.name.id"), event.getChannel().getId(), true)
                                .addField(LangManager.get(lang, "discord.log.channel.update.name.old"), oldName, true)
                                .addField(LangManager.get(lang, "discord.log.channel.update.name.new"), newName, true)
                                .addField(LangManager.get(lang, "discord.log.channel.update.name.executor"), finalExecutor, true)
                                .addField(LangManager.get(lang, "discord.log.channel.update.name.reason"), finalReason, false)
                        );
                    });
        }
    }

    @Override
    public void onChannelUpdateTopic(@NotNull ChannelUpdateTopicEvent event) {
        String lang = lang();

        String oldRaw = event.getOldValue();
        String newRaw = event.getNewValue();

        final String oldTopic = (oldRaw == null)
                ? LangManager.get(lang, "discord.log.common.none")
                : oldRaw;
        final String newTopic = (newRaw == null)
                ? LangManager.get(lang, "discord.log.common.none")
                : newRaw;

        if (!oldTopic.equals(newTopic)) {
            event.getGuild().retrieveAuditLogs()
                    .type(ActionType.CHANNEL_UPDATE)
                    .limit(5)
                    .queue(logs -> {
                        String executor = LangManager.get(lang, "discord.log.common.unknown");
                        String reason = LangManager.get(lang, "discord.log.common.no_reason");

                        Optional<AuditLogEntry> entry = logs.stream()
                                .filter(log -> log.getTargetId().equals(event.getChannel().getId()))
                                .filter(log -> System.currentTimeMillis() - log.getTimeCreated().toInstant().toEpochMilli() < 5000)
                                .findFirst();

                        if (entry.isPresent()) {
                            AuditLogEntry auditEntry = entry.get();
                            if (auditEntry.getUser() != null) {
                                executor = auditEntry.getUser().getName();
                            }
                            if (auditEntry.getReason() != null) {
                                reason = auditEntry.getReason();
                            }
                        }

                        final String finalExecutor = executor;
                        final String finalReason = reason;

                        LogManager.getInstance().sendEmbed(builder -> builder
                                .setTitle(LangManager.get(lang, "discord.log.channel.update.topic.title"))
                                .setColor(Color.YELLOW)
                                .addField(LangManager.get(lang, "discord.log.channel.update.topic.name"), event.getChannel().getName(), true)
                                .addField(LangManager.get(lang, "discord.log.channel.update.topic.id"), event.getChannel().getId(), true)
                                .addField(LangManager.get(lang, "discord.log.channel.update.topic.old"), oldTopic, false)
                                .addField(LangManager.get(lang, "discord.log.channel.update.topic.new"), newTopic, false)
                                .addField(LangManager.get(lang, "discord.log.channel.update.topic.executor"), finalExecutor, true)
                                .addField(LangManager.get(lang, "discord.log.channel.update.topic.reason"), finalReason, false)
                        );
                    });
        }
    }

    @Override
    public void onChannelUpdateNSFW(@NotNull ChannelUpdateNSFWEvent event) {
        String lang = lang();

        Boolean oldVal = event.getOldValue();
        Boolean newVal = event.getNewValue();

        if (oldVal != null && newVal != null && !oldVal.equals(newVal)) {
            final String oldText = oldVal
                    ? LangManager.get(lang, "discord.log.channel.update.nsfw.values.enabled")
                    : LangManager.get(lang, "discord.log.channel.update.nsfw.values.disabled");

            final String newText = newVal
                    ? LangManager.get(lang, "discord.log.channel.update.nsfw.values.enabled")
                    : LangManager.get(lang, "discord.log.channel.update.nsfw.values.disabled");

            event.getGuild().retrieveAuditLogs()
                    .type(ActionType.CHANNEL_UPDATE)
                    .limit(5)
                    .queue(logs -> {
                        String executor = LangManager.get(lang, "discord.log.common.unknown");
                        String reason = LangManager.get(lang, "discord.log.common.no_reason");

                        Optional<AuditLogEntry> entry = logs.stream()
                                .filter(log -> log.getTargetId().equals(event.getChannel().getId()))
                                .filter(log -> System.currentTimeMillis() - log.getTimeCreated().toInstant().toEpochMilli() < 5000)
                                .findFirst();

                        if (entry.isPresent()) {
                            AuditLogEntry auditEntry = entry.get();
                            if (auditEntry.getUser() != null) {
                                executor = auditEntry.getUser().getName();
                            }
                            if (auditEntry.getReason() != null) {
                                reason = auditEntry.getReason();
                            }
                        }

                        final String finalExecutor = executor;
                        final String finalReason = reason;

                        LogManager.getInstance().sendEmbed(builder -> builder
                                .setTitle(LangManager.get(lang, "discord.log.channel.update.nsfw.title"))
                                .setColor(Color.ORANGE)
                                .addField(LangManager.get(lang, "discord.log.channel.update.nsfw.name"), event.getChannel().getName(), true)
                                .addField(LangManager.get(lang, "discord.log.channel.update.nsfw.id"), event.getChannel().getId(), true)
                                .addField(LangManager.get(lang, "discord.log.channel.update.nsfw.old"), oldText, true)
                                .addField(LangManager.get(lang, "discord.log.channel.update.nsfw.new"), newText, true)
                                .addField(LangManager.get(lang, "discord.log.channel.update.nsfw.executor"), finalExecutor, true)
                                .addField(LangManager.get(lang, "discord.log.channel.update.nsfw.reason"), finalReason, false)
                        );
                    });
        }
    }

    @Override
    public void onChannelUpdateBitrate(@NotNull ChannelUpdateBitrateEvent event) {
        String lang = lang();

        Integer oldVal = event.getOldValue();
        Integer newVal = event.getNewValue();

        if (oldVal != null && newVal != null && !oldVal.equals(newVal)) {
            final String oldBitrate = oldVal + " kbps";
            final String newBitrate = newVal + " kbps";

            event.getGuild().retrieveAuditLogs()
                    .type(ActionType.CHANNEL_UPDATE)
                    .limit(5)
                    .queue(logs -> {
                        String executor = LangManager.get(lang, "discord.log.common.unknown");
                        String reason = LangManager.get(lang, "discord.log.common.no_reason");

                        Optional<AuditLogEntry> entry = logs.stream()
                                .filter(log -> log.getTargetId().equals(event.getChannel().getId()))
                                .filter(log -> System.currentTimeMillis() - log.getTimeCreated().toInstant().toEpochMilli() < 5000)
                                .findFirst();

                        if (entry.isPresent()) {
                            AuditLogEntry auditEntry = entry.get();
                            if (auditEntry.getUser() != null) {
                                executor = auditEntry.getUser().getName();
                            }
                            if (auditEntry.getReason() != null) {
                                reason = auditEntry.getReason();
                            }
                        }

                        final String finalExecutor = executor;
                        final String finalReason = reason;

                        LogManager.getInstance().sendEmbed(builder -> builder
                                .setTitle(LangManager.get(lang, "discord.log.channel.update.bitrate.title"))
                                .setColor(Color.CYAN)
                                .addField(LangManager.get(lang, "discord.log.channel.update.bitrate.name"), event.getChannel().getName(), true)
                                .addField(LangManager.get(lang, "discord.log.channel.update.bitrate.old"), oldBitrate, true)
                                .addField(LangManager.get(lang, "discord.log.channel.update.bitrate.new"), newBitrate, true)
                                .addField(LangManager.get(lang, "discord.log.channel.update.bitrate.executor"), finalExecutor, true)
                                .addField(LangManager.get(lang, "discord.log.channel.update.bitrate.reason"), finalReason, false)
                        );
                    });
        }
    }

    @Override
    public void onChannelUpdateUserLimit(@NotNull ChannelUpdateUserLimitEvent event) {
        String lang = lang();

        Integer oldVal = event.getOldValue();
        Integer newVal = event.getNewValue();

        if (oldVal != null && newVal != null && !oldVal.equals(newVal)) {
            final String oldLimit = (oldVal == 0)
                    ? LangManager.get(lang, "discord.log.common.none")
                    : oldVal.toString();
            final String newLimit = (newVal == 0)
                    ? LangManager.get(lang, "discord.log.common.none")
                    : newVal.toString();

            event.getGuild().retrieveAuditLogs()
                    .type(ActionType.CHANNEL_UPDATE)
                    .limit(5)
                    .queue(logs -> {
                        String executor = LangManager.get(lang, "discord.log.common.unknown");
                        String reason = LangManager.get(lang, "discord.log.common.no_reason");

                        Optional<AuditLogEntry> entry = logs.stream()
                                .filter(log -> log.getTargetId().equals(event.getChannel().getId()))
                                .filter(log -> System.currentTimeMillis() - log.getTimeCreated().toInstant().toEpochMilli() < 5000)
                                .findFirst();

                        if (entry.isPresent()) {
                            AuditLogEntry auditEntry = entry.get();
                            if (auditEntry.getUser() != null) {
                                executor = auditEntry.getUser().getName();
                            }
                            if (auditEntry.getReason() != null) {
                                reason = auditEntry.getReason();
                            }
                        }

                        final String finalExecutor = executor;
                        final String finalReason = reason;

                        LogManager.getInstance().sendEmbed(builder -> builder
                                .setTitle(LangManager.get(lang, "discord.log.channel.update.userlimit.title"))
                                .setColor(Color.CYAN)
                                .addField(LangManager.get(lang, "discord.log.channel.update.userlimit.name"), event.getChannel().getName(), true)
                                .addField(LangManager.get(lang, "discord.log.channel.update.userlimit.old"), oldLimit, true)
                                .addField(LangManager.get(lang, "discord.log.channel.update.userlimit.new"), newLimit, true)
                                .addField(LangManager.get(lang, "discord.log.channel.update.userlimit.executor"), finalExecutor, true)
                                .addField(LangManager.get(lang, "discord.log.channel.update.userlimit.reason"), finalReason, false)
                        );
                    });
        }
    }

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        String lang = lang();

        LogManager.getInstance().sendEmbed(builder -> builder
                .setTitle(LangManager.get(lang, "discord.log.voice.join.title"))
                .setColor(Color.GREEN)
                .addField(LangManager.get(lang, "discord.log.voice.join.user"), event.getMember().getUser().getName(), true)
                .addField(LangManager.get(lang, "discord.log.voice.join.user_id"), event.getMember().getUser().getId(), true)
                .addField(LangManager.get(lang, "discord.log.voice.join.channel"), event.getChannelJoined().getName(), true)
                .addField(LangManager.get(lang, "discord.log.voice.join.channel_id"), event.getChannelJoined().getId(), true)
        );
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        String lang = lang();

        LogManager.getInstance().sendEmbed(builder -> builder
                .setTitle(LangManager.get(lang, "discord.log.voice.leave.title"))
                .setColor(Color.RED)
                .addField(LangManager.get(lang, "discord.log.voice.leave.user"), event.getMember().getUser().getName(), true)
                .addField(LangManager.get(lang, "discord.log.voice.leave.user_id"), event.getMember().getUser().getId(), true)
                .addField(LangManager.get(lang, "discord.log.voice.leave.channel"), event.getChannelLeft().getName(), true)
                .addField(LangManager.get(lang, "discord.log.voice.leave.channel_id"), event.getChannelLeft().getId(), true)
        );
    }

    @Override
    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event) {
        String lang = lang();

        LogManager.getInstance().sendEmbed(builder -> builder
                .setTitle(LangManager.get(lang, "discord.log.voice.move.title"))
                .setColor(Color.ORANGE)
                .addField(LangManager.get(lang, "discord.log.voice.move.user"), event.getMember().getUser().getName(), true)
                .addField(LangManager.get(lang, "discord.log.voice.move.user_id"), event.getMember().getUser().getId(), true)
                .addField(LangManager.get(lang, "discord.log.voice.move.from"), event.getChannelLeft().getName(), true)
                .addField(LangManager.get(lang, "discord.log.voice.move.to"), event.getChannelJoined().getName(), true)
        );
    }

    @Override
    public void onGuildVoiceMute(@NotNull GuildVoiceMuteEvent event) {
        String lang = lang();

        String mutedText = event.isMuted()
                ? LangManager.get(lang, "discord.log.voice.mute.muted_yes")
                : LangManager.get(lang, "discord.log.voice.mute.muted_no");

        LogManager.getInstance().sendEmbed(builder -> builder
                .setTitle(LangManager.get(lang, "discord.log.voice.mute.title"))
                .setColor(Color.MAGENTA)
                .addField(LangManager.get(lang, "discord.log.voice.mute.user"), event.getMember().getUser().getName(), true)
                .addField(LangManager.get(lang, "discord.log.voice.mute.user_id"), event.getMember().getUser().getId(), true)
                .addField(LangManager.get(lang, "discord.log.voice.mute.muted_field"), mutedText, true)
        );
    }
    @Override
    public void onGuildVoiceDeafen(@NotNull GuildVoiceDeafenEvent event) {
        String lang = lang();

        String deafenedText = event.isDeafened()
                ? LangManager.get(lang, "discord.log.voice.deafen.deafened_yes")
                : LangManager.get(lang, "discord.log.voice.deafen.deafened_no");

        LogManager.getInstance().sendEmbed(builder -> builder
                .setTitle(LangManager.get(lang, "discord.log.voice.deafen.title"))
                .setColor(Color.MAGENTA)
                .addField(LangManager.get(lang, "discord.log.voice.deafen.user"), event.getMember().getUser().getName(), true)
                .addField(LangManager.get(lang, "discord.log.voice.deafen.user_id"), event.getMember().getUser().getId(), true)
                .addField(LangManager.get(lang, "discord.log.voice.deafen.deafened_field"), deafenedText, true)
        );
    }
}
