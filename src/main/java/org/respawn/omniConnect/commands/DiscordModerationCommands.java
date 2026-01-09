package org.respawn.omniConnect.discord;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.respawn.omniConnect.config.OmniConfig;
import org.respawn.omniConnect.link.LinkDatabase;
import org.respawn.omniConnect.moderation.ModerationAPIHandler;
import org.respawn.omniConnect.hooks.DiscordLog;

import java.util.UUID;

public class DiscordModerationCommands extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        // --- CONFIG ALAPÚ ENGEDÉLYEZÉS ---
        if (!OmniConfig.getBoolean("discord.discordmoderation.enabled")) {
            event.reply("❌ A Discord moderációs rendszer nincs engedélyezve.").setEphemeral(true).queue();
            return;
        }

        switch (event.getName()) {
            case "warn":
                handleWarn(event);
                break;
            case "mute":
                handleMute(event);
                break;
            case "kick":
                handleKick(event);
                break;
            case "ban":
                handleBan(event);
                break;
            case "timeout":
                handleTimeout(event);
                break;
        }
    }

    private UUID getLinkedUUID(Member target) {
        return LinkDatabase.getMinecraftUUID(target.getId());
    }

    private OfflinePlayer getOfflinePlayer(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid);
    }

    private String getLogChannel() {
        return OmniConfig.getString("discord.discordmoderation.log-channel");
    }

    private Member getTarget(SlashCommandInteractionEvent event) {
        if (event.getOption("target") == null) return null;
        return event.getOption("target").getAsMember();
    }

    private void handleWarn(SlashCommandInteractionEvent event) {
        Member target = getTarget(event);
        if (target == null) {
            event.reply("❌ A parancs csak szerveren belüli felhasználókra használható.").setEphemeral(true).queue();
            return;
        }

        String reason = event.getOption("reason").getAsString();
        String moderator = event.getUser().getName();

        UUID uuid = getLinkedUUID(target);
        if (uuid == null) {
            event.reply("❌ A felhasználó nincs összekötve Minecraft fiókkal.").setEphemeral(true).queue();
            return;
        }

        OfflinePlayer op = getOfflinePlayer(uuid);
        ModerationAPIHandler.warn(op, reason, moderator);

        event.reply("⚠️ Warn kiadva: **" + target.getEffectiveName() + "**").queue();

        DiscordLog.sendCategory(
                getLogChannel(),
                "⚠️ Discord Warn",
                "Moderátor: **" + moderator + "**\n" +
                        "Felhasználó: **" + target.getEffectiveName() + "**\n" +
                        "Minecraft: **" + op.getName() + "**\n" +
                        "Indok: `" + reason + "`"
        );
    }

    private void handleMute(SlashCommandInteractionEvent event) {
        Member target = getTarget(event);
        if (target == null) {
            event.reply("❌ A parancs csak szerveren belüli felhasználókra használható.").setEphemeral(true).queue();
            return;
        }

        String reason = event.getOption("reason").getAsString();
        String moderator = event.getUser().getName();

        UUID uuid = getLinkedUUID(target);
        if (uuid == null) {
            event.reply("❌ A felhasználó nincs összekötve Minecraft fiókkal.").setEphemeral(true).queue();
            return;
        }

        OfflinePlayer op = getOfflinePlayer(uuid);
        ModerationAPIHandler.mute(op, reason, moderator);

        event.reply("🔇 Mute kiadva: **" + target.getEffectiveName() + "**").queue();

        DiscordLog.sendCategory(
                getLogChannel(),
                "🔇 Discord Mute",
                "Moderátor: **" + moderator + "**\n" +
                        "Felhasználó: **" + target.getEffectiveName() + "**\n" +
                        "Minecraft: **" + op.getName() + "**\n" +
                        "Indok: `" + reason + "`"
        );
    }

    private void handleKick(SlashCommandInteractionEvent event) {
        Member target = getTarget(event);
        if (target == null) {
            event.reply("❌ A parancs csak szerveren belüli felhasználókra használható.").setEphemeral(true).queue();
            return;
        }

        String reason = event.getOption("reason").getAsString();
        String moderator = event.getUser().getName();

        UUID uuid = getLinkedUUID(target);
        if (uuid == null) {
            event.reply("❌ A felhasználó nincs összekötve Minecraft fiókkal.").setEphemeral(true).queue();
            return;
        }

        OfflinePlayer op = getOfflinePlayer(uuid);
        ModerationAPIHandler.kick(op, reason, moderator);

        event.reply("👢 Kick kiadva: **" + target.getEffectiveName() + "**").queue();

        DiscordLog.sendCategory(
                getLogChannel(),
                "👢 Discord Kick",
                "Moderátor: **" + moderator + "**\n" +
                        "Felhasználó: **" + target.getEffectiveName() + "**\n" +
                        "Minecraft: **" + op.getName() + "**\n" +
                        "Indok: `" + reason + "`"
        );
    }

    private void handleBan(SlashCommandInteractionEvent event) {
        Member target = getTarget(event);
        if (target == null) {
            event.reply("❌ A parancs csak szerveren belüli felhasználókra használható.").setEphemeral(true).queue();
            return;
        }

        String reason = event.getOption("reason").getAsString();
        String moderator = event.getUser().getName();

        UUID uuid = getLinkedUUID(target);
        if (uuid == null) {
            event.reply("❌ A felhasználó nincs összekötve Minecraft fiókkal.").setEphemeral(true).queue();
            return;
        }

        OfflinePlayer op = getOfflinePlayer(uuid);
        ModerationAPIHandler.ban(op, reason, moderator);

        event.reply("⛔ Ban kiadva: **" + target.getEffectiveName() + "**").queue();

        DiscordLog.sendCategory(
                getLogChannel(),
                "⛔ Discord Ban",
                "Moderátor: **" + moderator + "**\n" +
                        "Felhasználó: **" + target.getEffectiveName() + "**\n" +
                        "Minecraft: **" + op.getName() + "**\n" +
                        "Indok: `" + reason + "`"
        );
    }

    private void handleTimeout(SlashCommandInteractionEvent event) {
        Member target = getTarget(event);
        if (target == null) {
            event.reply("❌ A parancs csak szerveren belüli felhasználókra használható.").setEphemeral(true).queue();
            return;
        }

        long minutes = event.getOption("minutes").getAsLong();
        String reason = event.getOption("reason").getAsString();
        String moderator = event.getUser().getName();

        UUID uuid = getLinkedUUID(target);
        if (uuid == null) {
            event.reply("❌ A felhasználó nincs összekötve Minecraft fiókkal.").setEphemeral(true).queue();
            return;
        }

        OfflinePlayer op = getOfflinePlayer(uuid);
        ModerationAPIHandler.tempMute(op, minutes, reason, moderator);

        event.reply("⏳ Timeout / tempmute kiadva: **" + target.getEffectiveName() + "** (" + minutes + " perc)").queue();

        DiscordLog.sendCategory(
                getLogChannel(),
                "⏳ Discord Timeout / TempMute",
                "Moderátor: **" + moderator + "**\n" +
                        "Felhasználó: **" + target.getEffectiveName() + "**\n" +
                        "Minecraft: **" + op.getName() + "**\n" +
                        "Időtartam: `" + minutes + " perc`\n" +
                        "Indok: `" + reason + "`"
        );
    }
}
