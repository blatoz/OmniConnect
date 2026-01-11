package org.respawn.omniConnect.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

import java.awt.*;
import java.time.Instant;

public class DiscordModerationCommands extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        if (!isModerationEnabled()) {
            String lang = LangManager.getDefaultLanguage();
            sendErrorEmbed(event, LangManager.get(lang, "errors.moderation_disabled"));
            return;
        }

        switch (event.getName()) {
            case "warn":    handleWarn(event);    break;
            case "kick":    handleKick(event);    break;
            case "ban":     handleBan(event);     break;
            case "timeout": handleTimeout(event); break;
        }
    }

    // ---------------------------------------------------------
    // Helper metódusok
    // ---------------------------------------------------------

    private boolean isModerationEnabled() {
        return Main.getInstance().getConfig().getBoolean("discord.discordmoderation.enabled", true);
    }

    private Member getMemberOption(SlashCommandInteractionEvent event, String name) {
        if (event.getOption(name) == null) return null;
        try { return event.getOption(name).getAsMember(); }
        catch (Exception e) { return null; }
    }

    private String getStringOption(SlashCommandInteractionEvent event, String name) {
        if (event.getOption(name) == null) return null;
        try { return event.getOption(name).getAsString(); }
        catch (Exception e) { return null; }
    }

    private boolean hasPermission(Member member, Permission perm) {
        return member != null && member.hasPermission(perm);
    }

    private void sendErrorEmbed(SlashCommandInteractionEvent event, String message) {
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.RED)
                .setDescription(message)
                .setTimestamp(Instant.now());

        event.replyEmbeds(embed.build()).setEphemeral(true).queue();
    }

    private void sendSuccessEmbed(SlashCommandInteractionEvent event, String title, String description) {
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(title)
                .setDescription(description)
                .setTimestamp(Instant.now());

        event.replyEmbeds(embed.build()).queue();
    }

    private void logModeration(String title, String description) {
        DiscordLog.sendCategory("discord.discordmoderation.log-channel", title, description);
    }

    // ---------------------------------------------------------
    // MODERÁCIÓS PARANCSOK
    // ---------------------------------------------------------

    private void handleWarn(SlashCommandInteractionEvent event) {
        String lang = LangManager.getDefaultLanguage();

        if (!hasPermission(event.getMember(), Permission.MODERATE_MEMBERS)) {
            sendErrorEmbed(event, LangManager.get(lang, "errors.no_permission"));
            return;
        }

        Member target = getMemberOption(event, "user");
        String reason = getStringOption(event, "reason");

        if (target == null) {
            sendErrorEmbed(event, LangManager.get(lang, "errors.no_user"));
            return;
        }

        if (reason == null || reason.isEmpty()) {
            reason = LangManager.get(lang, "discord.moderation.no_reason");
        }

        String title = LangManager.get(lang, "discord.moderation.warn.title");
        String desc = LangManager.get(lang, "discord.moderation.warn.description")
                .replace("%user%", target.getEffectiveName())
                .replace("%reason%", reason);

        sendSuccessEmbed(event, title, desc);
        logModeration(title, desc);
    }

    private void handleKick(SlashCommandInteractionEvent event) {
        String lang = LangManager.getDefaultLanguage();

        if (!hasPermission(event.getMember(), Permission.KICK_MEMBERS)) {
            sendErrorEmbed(event, LangManager.get(lang, "errors.no_permission"));
            return;
        }

        Member target = getMemberOption(event, "user");
        String reason = getStringOption(event, "reason");

        if (target == null) {
            sendErrorEmbed(event, LangManager.get(lang, "errors.no_user"));
            return;
        }

        if (reason == null || reason.isEmpty()) {
            reason = LangManager.get(lang, "discord.moderation.no_reason");
        }

        target.kick(reason).queue();

        String title = LangManager.get(lang, "discord.moderation.kick.title");
        String desc = LangManager.get(lang, "discord.moderation.kick.description")
                .replace("%user%", target.getEffectiveName())
                .replace("%reason%", reason);

        sendSuccessEmbed(event, title, desc);
        logModeration(title, desc);
    }

    private void handleBan(SlashCommandInteractionEvent event) {
        String lang = LangManager.getDefaultLanguage();

        if (!hasPermission(event.getMember(), Permission.BAN_MEMBERS)) {
            sendErrorEmbed(event, LangManager.get(lang, "errors.no_permission"));
            return;
        }

        Member target = getMemberOption(event, "user");
        String reason = getStringOption(event, "reason");

        if (target == null) {
            sendErrorEmbed(event, LangManager.get(lang, "errors.no_user"));
            return;
        }

        if (reason == null || reason.isEmpty()) {
            reason = LangManager.get(lang, "discord.moderation.no_reason");
        }

        target.ban(7, reason).queue();

        String title = LangManager.get(lang, "discord.moderation.ban.title");
        String desc = LangManager.get(lang, "discord.moderation.ban.description")
                .replace("%user%", target.getEffectiveName())
                .replace("%reason%", reason);

        sendSuccessEmbed(event, title, desc);
        logModeration(title, desc);
    }

    private void handleTimeout(SlashCommandInteractionEvent event) {
        String lang = LangManager.getDefaultLanguage();

        if (!hasPermission(event.getMember(), Permission.MODERATE_MEMBERS)) {
            sendErrorEmbed(event, LangManager.get(lang, "errors.no_permission"));
            return;
        }

        Member target = getMemberOption(event, "user");
        int minutes = 0;

        if (event.getOption("minutes") != null) {
            try { minutes = event.getOption("minutes").getAsInt(); }
            catch (Exception ignored) {}
        }

        if (target == null) {
            sendErrorEmbed(event, LangManager.get(lang, "errors.no_user"));
            return;
        }

        if (minutes <= 0) {
            sendErrorEmbed(event, LangManager.get(lang, "errors.invalid_number"));
            return;
        }

        target.timeoutFor(java.time.Duration.ofMinutes(minutes)).queue();

        String title = LangManager.get(lang, "discord.moderation.timeout.title");
        String desc = LangManager.get(lang, "discord.moderation.timeout.description")
                .replace("%user%", target.getEffectiveName())
                .replace("%minutes%", String.valueOf(minutes));

        sendSuccessEmbed(event, title, desc);
        logModeration(title, desc);
    }
}
