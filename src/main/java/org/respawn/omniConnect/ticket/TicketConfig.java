package org.respawn.omniConnect.ticket;

import org.bukkit.configuration.file.FileConfiguration;
import org.respawn.omniConnect.Main;

import java.util.Set;

public class TicketConfig {

    private static FileConfiguration cfg;

    public static void load() {
        cfg = Main.getInstance().getConfig();
    }

    public static String getGuildId() {
        return cfg.getString("discord.ticket.guild-id", "");
    }

    public static Set<String> getTypes() {
        return cfg.getConfigurationSection("discord.ticket.types").getKeys(false);
    }

    public static String getCategory(TicketType type) {
        return cfg.getString("discord.ticket.types." + type.name().toLowerCase() + ".category-id", "");
    }

    public static String getPanelChannel(TicketType type) {
        return cfg.getString("discord.ticket.types." + type.name().toLowerCase() + ".panel-channel-id", "");
    }

    public static String getLogChannel(TicketType type) {
        return cfg.getString("discord.ticket.types." + type.name().toLowerCase() + ".log-channel-id", "");
    }

    public static String getTranscriptChannel(TicketType type) {
        return cfg.getString("discord.ticket.types." + type.name().toLowerCase() + ".transcript-channel-id", "");
    }

    public static String getStaffRole(TicketType type) {
        return cfg.getString("discord.ticket.types." + type.name().toLowerCase() + ".staff-role-id", "");
    }

    public static String getTicketNameFormat(TicketType type) {
        return cfg.getString("discord.ticket.types." + type.name().toLowerCase() + ".ticket-name-format", "ticket-%player%");
    }

    public static boolean isTranscriptEnabled(TicketType type) {
        return cfg.getBoolean("discord.ticket.types." + type.name().toLowerCase() + ".transcript.enabled", true);
    }

    public static String getTranscriptFormat(TicketType type) {
        return cfg.getString("discord.ticket.types." + type.name().toLowerCase() + ".transcript.format", "html");
    }
}
