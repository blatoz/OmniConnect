// DiscordListener.java – teljes, javított, cache-es, csatorna-update-es verzió
// (A teljes kódot ide illesztettem, hogy könnyen szerkeszthető legyen Pages-ben.)

package org.respawn.omniConnect;

import net.dv8tion.jda.api.entities.Message;
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

import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Discord esemény figyelő - naplózza az összes szerver- és felhasználó-tevékenységet.
 * Kezeli az üzenetek, csatornák, szerepek és voice csatornák eseményeit.
 */
public class DiscordListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!event.isFromGuild()) return;

        Message msg = event.getMessage();
        MessageCache.store(msg);

        final String attachments = msg.getAttachments().isEmpty()
                ? "Nincs csatolmány"
                : msg.getAttachments().stream()
                .map(a -> a.getFileName() + " (" + a.getUrl() + ")")
                .collect(Collectors.joining("\n"));

        LogManager.getInstance().sendEmbed(builder -> builder
                .setTitle("Új Üzenet")
                .setColor(Color.CYAN)
                .addField("Felhasználó", event.getAuthor().getName(), true)
                .addField("Felhasználó ID", event.getAuthor().getId(), true)
                .addField("Csatorna", event.getChannel().getName(), true)
                .addField("Csatorna ID", event.getChannel().getId(), true)
                .addField("Tartalom", "```\n" + msg.getContentRaw() + "\n```", false)
                .addField("Csatolmányok", attachments, false)
                .addField("Üzenet ID", msg.getId(), true)
        );
    }

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {
        if (!event.isFromGuild()) return;

        Message cached = MessageCache.get(event.getMessageId());

        final String contentFinal;
        final String attachmentsFinal;
        final String authorFinal;

        if (cached != null) {
            contentFinal = cached.getContentRaw().isEmpty()
                    ? "Nincs szöveges tartalom"
                    : cached.getContentRaw();

            attachmentsFinal = cached.getAttachments().isEmpty()
                    ? "Nincs csatolmány"
                    : cached.getAttachments().stream()
                    .map(a -> a.getFileName() + " (" + a.getUrl() + ")")
                    .collect(Collectors.joining("\n"));

            authorFinal = cached.getAuthor().getName();
        } else {
            contentFinal = "Nem elérhető (a bot nem látta az üzenetet)";
            attachmentsFinal = "Nincs csatolmány";
            authorFinal = "Ismeretlen";
        }

        LogManager.getInstance().sendEmbed(builder -> builder
                .setTitle("Üzenet Törölve")
                .setColor(Color.ORANGE)
                .addField("Csatorna", event.getChannel().getName(), true)
                .addField("Csatorna ID", event.getChannel().getId(), true)
                .addField("Felhasználó", authorFinal, true)
                .addField("Üzenet tartalma", "```\n" + contentFinal + "\n```", false)
                .addField("Csatolmányok", attachmentsFinal, false)
                .addField("Üzenet ID", event.getMessageId(), true)
        );

        MessageCache.remove(event.getMessageId());
    }

    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!event.isFromGuild()) return;

        Message msg = event.getMessage();
        MessageCache.store(msg);

        final String attachments = msg.getAttachments().isEmpty()
                ? "Nincs csatolmány"
                : msg.getAttachments().stream()
                .map(a -> a.getFileName() + " (" + a.getUrl() + ")")
                .collect(Collectors.joining("\n"));

        LogManager.getInstance().sendEmbed(builder -> builder
                .setTitle("Üzenet Szerkesztve")
                .setColor(Color.YELLOW)
                .addField("Felhasználó", event.getAuthor().getName(), true)
                .addField("Felhasználó ID", event.getAuthor().getId(), true)
                .addField("Csatorna", event.getChannel().getName(), true)
                .addField("Új tartalom", "```\n" + msg.getContentRaw() + "\n```", false)
                .addField("Csatolmányok", attachments, false)
                .addField("Üzenet ID", msg.getId(), true)
        );
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        LogManager.getInstance().sendEmbed(builder -> builder
                .setTitle("Tag Csatlakozott")
                .setColor(Color.GREEN)
                .addField("Felhasználó", event.getUser().getName(), false)
                .addField("Felhasználó ID", event.getUser().getId(), false)
                .addField("Létrehozva", event.getUser().getTimeCreated().toString(), false)
                .addField("Bot?", event.getUser().isBot() ? "Igen" : "Nem", false)
        );
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        LogManager.getInstance().sendEmbed(builder -> builder
                .setTitle("Tag Kilépett")
                .setColor(Color.RED)
                .addField("Felhasználó", event.getUser().getName(), false)
                .addField("Felhasználó ID", event.getUser().getId(), false)
                .addField("Létrehozva", event.getUser().getTimeCreated().toString(), false)
                .addField("Bot?", event.getUser().isBot() ? "Igen" : "Nem", false)
        );
    }

    @Override
    public void onUserUpdateName(@NotNull UserUpdateNameEvent event) {
        final String oldName = event.getOldValue();
        final String newName = event.getNewValue();

        LogManager.getInstance().sendEmbed(builder -> builder
                .setTitle("Felhasználó Neve Megváltozott")
                .setColor(Color.YELLOW)
                .addField("Felhasználó ID", event.getUser().getId(), true)
                .addField("Régi név", oldName, true)
                .addField("Új név", newName, true)
        );
    }

    @Override
    public void onUserUpdateAvatar(@NotNull UserUpdateAvatarEvent event) {
        final String oldAvatarUrl = event.getOldValue();
        final String newAvatarUrl = event.getNewValue();

        LogManager.getInstance().sendEmbed(builder -> builder
                .setTitle("Felhasználó Avatárja Megváltozott")
                .setColor(Color.CYAN)
                .addField("Felhasználó", event.getUser().getName(), true)
                .addField("Felhasználó ID", event.getUser().getId(), true)
                .addField("Régi avatar", oldAvatarUrl != null ? oldAvatarUrl : "Nincs", false)
                .addField("Új avatar", newAvatarUrl != null ? newAvatarUrl : "Nincs", false)
        );
    }

    @Override
    public void onUserUpdateDiscriminator(@NotNull UserUpdateDiscriminatorEvent event) {
        final String oldDiscriminator = event.getOldValue();
        final String newDiscriminator = event.getNewValue();

        LogManager.getInstance().sendEmbed(builder -> builder
                .setTitle("Felhasználó Discriminatora Megváltozott")
                .setColor(Color.LIGHT_GRAY)
                .addField("Felhasználó", event.getUser().getName(), true)
                .addField("Felhasználó ID", event.getUser().getId(), true)
                .addField("Régi discriminator", oldDiscriminator, true)
                .addField("Új discriminator", newDiscriminator, true)
        );
    }

    @Override
    public void onGuildBan(@NotNull GuildBanEvent event) {
        // Audit log lekérése
        event.getGuild().retrieveAuditLogs()
                .type(ActionType.BAN)
                .limit(5)
                .queue(logs -> {
                    String executor = "Ismeretlen";
                    String reason = "Nincs indok megadva";

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
                            .setTitle("Tag Kitiltva")
                            .setColor(Color.RED)
                            .addField("Felhasználó", event.getUser().getName(), true)
                            .addField("Felhasználó ID", event.getUser().getId(), true)
                            .addField("Kitiltotta", finalExecutor, true)
                            .addField("Indoklás", finalReason, false)
                    );
                });
    }

    @Override
    public void onGuildUnban(@NotNull GuildUnbanEvent event) {
        // Audit log lekérése
        event.getGuild().retrieveAuditLogs()
                .type(ActionType.UNBAN)
                .limit(5)
                .queue(logs -> {
                    String executor = "Ismeretlen";
                    String reason = "Nincs indok megadva";

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
                            .setTitle("Kitiltás Feloldva")
                            .setColor(Color.GREEN)
                            .addField("Felhasználó", event.getUser().getName(), true)
                            .addField("Felhasználó ID", event.getUser().getId(), true)
                            .addField("Feloldotta", finalExecutor, true)
                            .addField("Indoklás", finalReason, false)
                    );
                });
    }

    @Override
    public void onGuildMemberRoleAdd(@NotNull GuildMemberRoleAddEvent event) {
        final String roles = event.getRoles().stream()
                .map(net.dv8tion.jda.api.entities.Role::getName)
                .collect(Collectors.joining(", "));

        // Audit log lekérése
        event.getGuild().retrieveAuditLogs()
                .type(ActionType.MEMBER_ROLE_UPDATE)
                .limit(5)
                .queue(logs -> {
                    String executor = "Ismeretlen";
                    String reason = "Nincs indok megadva";

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
                            .setTitle("Szerep Hozzáadva")
                            .setColor(Color.MAGENTA)
                            .addField("Felhasználó", event.getUser().getName(), true)
                            .addField("Felhasználó ID", event.getUser().getId(), true)
                            .addField("Kapott szerepek", roles, false)
                            .addField("Végrehajtó", finalExecutor, true)
                            .addField("Indoklás", finalReason, false)
                    );
                });
    }

    @Override
    public void onGuildMemberRoleRemove(@NotNull GuildMemberRoleRemoveEvent event) {
        final String roles = event.getRoles().stream()
                .map(net.dv8tion.jda.api.entities.Role::getName)
                .collect(Collectors.joining(", "));

        // Audit log lekérése
        event.getGuild().retrieveAuditLogs()
                .type(ActionType.MEMBER_ROLE_UPDATE)
                .limit(5)
                .queue(logs -> {
                    String executor = "Ismeretlen";
                    String reason = "Nincs indok megadva";

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
                            .setTitle("Szerep Eltávolítva")
                            .setColor(Color.PINK)
                            .addField("Felhasználó", event.getUser().getName(), true)
                            .addField("Felhasználó ID", event.getUser().getId(), true)
                            .addField("Eltávolított szerepek", roles, false)
                            .addField("Végrehajtó", finalExecutor, true)
                            .addField("Indoklás", finalReason, false)
                    );
                });
    }

    @Override
    public void onChannelCreate(@NotNull ChannelCreateEvent event) {
        // Audit log lekérése
        event.getGuild().retrieveAuditLogs()
                .type(ActionType.CHANNEL_CREATE)
                .limit(5)
                .queue(logs -> {
                    String executor = "Ismeretlen";
                    String reason = "Nincs indok megadva";

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
                            .setTitle("Csatorna Létrehozva")
                            .setColor(Color.BLUE)
                            .addField("Csatorna", event.getChannel().getName(), false)
                            .addField("Csatorna ID", event.getChannel().getId(), false)
                            .addField("Csatorna Típusa", event.getChannel().getType().toString(), false)
                            .addField("Létrehozta", finalExecutor, true)
                            .addField("Indoklás", finalReason, false)
                    );
                });
    }

    @Override
    public void onChannelDelete(@NotNull ChannelDeleteEvent event) {
        // Audit log lekérése
        event.getGuild().retrieveAuditLogs()
                .type(ActionType.CHANNEL_DELETE)
                .limit(5)
                .queue(logs -> {
                    String executor = "Ismeretlen";
                    String reason = "Nincs indok megadva";

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
                            .setTitle("Csatorna Törölve")
                            .setColor(Color.RED)
                            .addField("Csatorna", event.getChannel().getName(), false)
                            .addField("Csatorna ID", event.getChannel().getId(), false)
                            .addField("Csatorna Típusa", event.getChannel().getType().toString(), false)
                            .addField("Törölte", finalExecutor, true)
                            .addField("Indoklás", finalReason, false)
                    );
                });
    }

    @Override
    public void onChannelUpdateName(@NotNull ChannelUpdateNameEvent event) {
        final String oldName = event.getOldValue();
        final String newName = event.getNewValue();

        if (oldName != null && newName != null && !oldName.equals(newName)) {
            // Audit log lekérése
            event.getGuild().retrieveAuditLogs()
                    .type(ActionType.CHANNEL_UPDATE)
                    .limit(5)
                    .queue(logs -> {
                        String executor = "Ismeretlen";
                        String reason = "Nincs indok megadva";

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
                                .setTitle("Csatorna Neve Frissítve")
                                .setColor(Color.YELLOW)
                                .addField("Csatorna ID", event.getChannel().getId(), true)
                                .addField("Régi név", oldName, true)
                                .addField("Új név", newName, true)
                                .addField("Módosította", finalExecutor, true)
                                .addField("Indoklás", finalReason, false)
                        );
                    });
        }
    }

    @Override
    public void onChannelUpdateTopic(@NotNull ChannelUpdateTopicEvent event) {
        String oldRaw = event.getOldValue();
        String newRaw = event.getNewValue();

        final String oldTopic = (oldRaw == null) ? "*Nem volt téma megadva*" : oldRaw;
        final String newTopic = (newRaw == null) ? "*Nem lett téma megadva*" : newRaw;

        if (!oldTopic.equals(newTopic)) {
            // Audit log lekérése
            event.getGuild().retrieveAuditLogs()
                    .type(ActionType.CHANNEL_UPDATE)
                    .limit(5)
                    .queue(logs -> {
                        String executor = "Ismeretlen";
                        String reason = "Nincs indok megadva";

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
                                .setTitle("Csatorna Téma frissítve")
                                .setColor(Color.YELLOW)
                                .addField("Csatorna", event.getChannel().getName(), true)
                                .addField("Csatorna ID", event.getChannel().getId(), true)
                                .addField("Régi Téma", oldTopic, false)
                                .addField("Új Téma", newTopic, false)
                                .addField("Módosította", finalExecutor, true)
                                .addField("Indoklás", finalReason, false)
                        );
                    });
        }
    }

    @Override
    public void onChannelUpdateNSFW(@NotNull ChannelUpdateNSFWEvent event) {
        Boolean oldVal = event.getOldValue();
        Boolean newVal = event.getNewValue();

        if (oldVal != null && newVal != null && !oldVal.equals(newVal)) {
            // Audit log lekérése
            event.getGuild().retrieveAuditLogs()
                    .type(ActionType.CHANNEL_UPDATE)
                    .limit(5)
                    .queue(logs -> {
                        String executor = "Ismeretlen";
                        String reason = "Nincs indok megadva";

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
                                .setTitle("NSFW Beállítás Frissítve")
                                .setColor(Color.ORANGE)
                                .addField("Csatorna", event.getChannel().getName(), true)
                                .addField("Csatorna ID", event.getChannel().getId(), true)
                                .addField("Régi Érték", oldVal ? "NSFW" : "Nem NSFW", true)
                                .addField("Új Érték", newVal ? "NSFW" : "Nem NSFW", true)
                                .addField("Módosította", finalExecutor, true)
                                .addField("Indoklás", finalReason, false)
                        );
                    });
        }
    }

    @Override
    public void onChannelUpdateBitrate(@NotNull ChannelUpdateBitrateEvent event) {
        Integer oldVal = event.getOldValue();
        Integer newVal = event.getNewValue();

        if (oldVal != null && newVal != null && !oldVal.equals(newVal)) {
            final String oldBitrate = oldVal + " kbps";
            final String newBitrate = newVal + " kbps";

            // Audit log lekérése
            event.getGuild().retrieveAuditLogs()
                    .type(ActionType.CHANNEL_UPDATE)
                    .limit(5)
                    .queue(logs -> {
                        String executor = "Ismeretlen";
                        String reason = "Nincs indok megadva";

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
                                .setTitle("Hang Csatorna Bitráta Frissítve")
                                .setColor(Color.CYAN)
                                .addField("Csatorna", event.getChannel().getName(), true)
                                .addField("Régi Bitráta", oldBitrate, true)
                                .addField("Új Bitráta", newBitrate, true)
                                .addField("Módosította", finalExecutor, true)
                                .addField("Indoklás", finalReason, false)
                        );
                    });
        }
    }

    @Override
    public void onChannelUpdateUserLimit(@NotNull ChannelUpdateUserLimitEvent event) {
        Integer oldVal = event.getOldValue();
        Integer newVal = event.getNewValue();

        if (oldVal != null && newVal != null && !oldVal.equals(newVal)) {
            final String oldLimit = (oldVal == 0) ? "Nincs limit" : oldVal.toString();
            final String newLimit = (newVal == 0) ? "Nincs limit" : newVal.toString();

            // Audit log lekérése
            event.getGuild().retrieveAuditLogs()
                    .type(ActionType.CHANNEL_UPDATE)
                    .limit(5)
                    .queue(logs -> {
                        String executor = "Ismeretlen";
                        String reason = "Nincs indok megadva";

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
                                .setTitle("Hang Csatorna Felhasználó Limit Frissítve")
                                .setColor(Color.CYAN)
                                .addField("Csatorna", event.getChannel().getName(), true)
                                .addField("Régi Limit", oldLimit, true)
                                .addField("Új Limit", newLimit, true)
                                .addField("Módosította", finalExecutor, true)
                                .addField("Indoklás", finalReason, false)
                        );
                    });
        }
    }

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        LogManager.getInstance().sendEmbed(builder -> builder
                .setTitle("Voice Csatlakozás")
                .setColor(Color.GREEN)
                .addField("Felhasználó", event.getMember().getUser().getName(), true)
                .addField("Felhasználó ID", event.getMember().getUser().getId(), true)
                .addField("Csatorna", event.getChannelJoined().getName(), true)
                .addField("Csatorna ID", event.getChannelJoined().getId(), true)
        );
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        LogManager.getInstance().sendEmbed(builder -> builder
                .setTitle("Voice Kilépés")
                .setColor(Color.RED)
                .addField("Felhasználó", event.getMember().getUser().getName(), true)
                .addField("Felhasználó ID", event.getMember().getUser().getId(), true)
                .addField("Csatorna", event.getChannelLeft().getName(), true)
                .addField("Csatorna ID", event.getChannelLeft().getId(), true)
        );
    }

    @Override
    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event) {
        LogManager.getInstance().sendEmbed(builder -> builder
                .setTitle("Voice Csatorna Váltás")
                .setColor(Color.ORANGE)
                .addField("Felhasználó", event.getMember().getUser().getName(), true)
                .addField("Felhasználó ID", event.getMember().getUser().getId(), true)
                .addField("Előző Csatorna", event.getChannelLeft().getName(), true)
                .addField("Előző Csatorna ID", event.getChannelLeft().getId(), true)
                .addField("Új Csatorna", event.getChannelJoined().getName(), true)
                .addField("Új Csatorna ID", event.getChannelJoined().getId(), true)
        );
    }
}