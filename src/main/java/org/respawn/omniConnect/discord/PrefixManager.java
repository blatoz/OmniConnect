package org.respawn.omniConnect.discord;

import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.database.PrefixDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PrefixManager {

    private static String globalPrefix;
    private static boolean perGuild;

    public static void load() {
        globalPrefix = Main.getInstance().getConfig().getString("discord.prefix", "!");
        perGuild = Main.getInstance().getConfig().getBoolean("discord.per-guild-prefix", false);
    }

    public static String getPrefix(String guildId) {
        if (!perGuild) return globalPrefix;

        try (Connection conn = PrefixDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT prefix FROM omniconnect_prefixes WHERE guild_id=?")) {

            ps.setString(1, guildId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getString("prefix");

        } catch (Exception ignored) {}

        return globalPrefix;
    }

    public static void setPrefix(String guildId, String prefix) {
        try (Connection conn = PrefixDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "REPLACE INTO omniconnect_prefixes (guild_id, prefix) VALUES (?, ?)")) {

            ps.setString(1, guildId);
            ps.setString(2, prefix);
            ps.executeUpdate();

        } catch (Exception ignored) {}
    }
}
