package org.respawn.omniConnect.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.lang.LangManager;

import java.awt.*;
import java.time.Instant;

public class DiscordUtilsCommands extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        if (!isUtilsModuleEnabled()) {
            String lang = LangManager.getDefaultLanguage();
            sendErrorEmbed(event, LangManager.get(lang, "errors.utils_disabled"));
            return;
        }

        switch (event.getName()) {
            case "ping":        handlePing(event);        break;
            case "avatar":      handleAvatar(event);      break;
            case "userinfo":    handleUserInfo(event);    break;
            case "serverinfo":  handleServerInfo(event);  break;
            case "mclink":      handleMcLink(event);      break;
            case "mcstatus":    handleMcStatus(event);    break;
        }
    }

    // ---------------------------------------------------------
    // Helper metódusok
    // ---------------------------------------------------------

    private boolean isUtilsModuleEnabled() {
        return Main.getInstance().getConfig().getBoolean("discord.utils.enabled", true);
    }

    private Member getMemberOption(SlashCommandInteractionEvent event, String name) {
        if (event.getOption(name) == null) return null;
        try { return event.getOption(name).getAsMember(); }
        catch (Exception e) { return null; }
    }

    private void sendErrorEmbed(SlashCommandInteractionEvent event, String message) {
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.RED)
                .setDescription(message)
                .setTimestamp(Instant.now());

        event.replyEmbeds(embed.build()).setEphemeral(true).queue();
    }

    private void sendEmbed(SlashCommandInteractionEvent event, EmbedBuilder embed) {
        embed.setTimestamp(Instant.now());
        event.replyEmbeds(embed.build()).queue();
    }

    // ---------------------------------------------------------
    // UTILS PARANCSOK
    // ---------------------------------------------------------

    private void handlePing(SlashCommandInteractionEvent event) {
        String lang = LangManager.getDefaultLanguage();
        long ping = event.getJDA().getGatewayPing();

        String title = LangManager.get(lang, "discord.utils.ping_title");
        String desc = LangManager.get(lang, "discord.utils.ping")
                .replace("%ping%", String.valueOf(ping));

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(title)
                .setDescription(desc);

        sendEmbed(event, embed);
    }

    private void handleAvatar(SlashCommandInteractionEvent event) {
        String lang = LangManager.getDefaultLanguage();

        Member target = getMemberOption(event, "user");
        if (target == null) target = event.getMember();
        if (target == null) {
            sendErrorEmbed(event, LangManager.get(lang, "errors.no_user"));
            return;
        }

        User user = target.getUser();

        String title = LangManager.get(lang, "discord.utils.avatar_title")
                .replace("%user%", user.getName());

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.CYAN)
                .setTitle(title)
                .setImage(user.getEffectiveAvatarUrl());

        sendEmbed(event, embed);
    }

    private void handleUserInfo(SlashCommandInteractionEvent event) {
        String lang = LangManager.getDefaultLanguage();

        Member target = getMemberOption(event, "user");
        if (target == null) target = event.getMember();
        if (target == null) {
            sendErrorEmbed(event, LangManager.get(lang, "errors.no_user"));
            return;
        }

        User user = target.getUser();

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(LangManager.get(lang, "discord.utils.userinfo_title"))
                .addField(LangManager.get(lang, "discord.utils.userinfo_name"), user.getName(), false)
                .addField(LangManager.get(lang, "discord.utils.userinfo_id"), user.getId(), false)
                .addField(LangManager.get(lang, "discord.utils.userinfo_created"),
                        user.getTimeCreated().toString(), false)
                .setThumbnail(user.getEffectiveAvatarUrl());

        sendEmbed(event, embed);
    }

    private void handleServerInfo(SlashCommandInteractionEvent event) {
        String lang = LangManager.getDefaultLanguage();

        if (event.getGuild() == null) {
            sendErrorEmbed(event, LangManager.get(lang, "errors.only_guild"));
            return;
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.ORANGE)
                .setTitle(LangManager.get(lang, "discord.utils.serverinfo_title"))
                .addField(LangManager.get(lang, "discord.utils.serverinfo_name"),
                        event.getGuild().getName(), false)
                .addField(LangManager.get(lang, "discord.utils.serverinfo_id"),
                        event.getGuild().getId(), false)
                .addField(LangManager.get(lang, "discord.utils.serverinfo_members"),
                        String.valueOf(event.getGuild().getMemberCount()), false)
                .setThumbnail(event.getGuild().getIconUrl());

        sendEmbed(event, embed);
    }

    private void handleMcLink(SlashCommandInteractionEvent event) {
        String lang = LangManager.getDefaultLanguage();

        // TODO: Integráld a saját LinkDatabase rendszereddel
        boolean linked = false;

        if (!linked) {
            sendErrorEmbed(event, LangManager.get(lang, "discord.utils.mclink_none"));
            return;
        }

        String name = "Steve"; // placeholder
        String uuid = "1234-5678"; // placeholder

        String desc = LangManager.get(lang, "discord.utils.mclink")
                .replace("%name%", name)
                .replace("%uuid%", uuid);

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.BLUE)
                .setTitle(LangManager.get(lang, "discord.utils.mclink_title"))
                .setDescription(desc);

        sendEmbed(event, embed);
    }

    private void handleMcStatus(SlashCommandInteractionEvent event) {
        String lang = LangManager.getDefaultLanguage();

        boolean online = true; // TODO: Minecraft ping rendszer

        String title = LangManager.get(lang, "discord.utils.mcstatus_title");
        String desc = online
                ? LangManager.get(lang, "discord.utils.mcstatus_online")
                : LangManager.get(lang, "discord.utils.mcstatus_offline");

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(online ? Color.GREEN : Color.RED)
                .setTitle(title)
                .setDescription(desc);

        sendEmbed(event, embed);
    }
}
