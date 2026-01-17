package org.respawn.omniConnect.discord;

import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.database.PrefixDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PrefixStorageProvider {

    public static String getPrefix(String guildId) {
        String mode = Main.getInstance().getConfig().getString("discord.prefix-storage", "yaml");

        if (mode.equalsIgnoreCase("yaml")) {
            return Main.getInstance().getConfig().getString("discord.prefix", "!");
        }

        try (Connection conn = PrefixDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT prefix FROM omniconnect_prefixes WHERE guild_id=?")) {

            ps.setString(1, guildId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getString("prefix");

        } catch (Exception ignored) {}

        return Main.getInstance().getConfig().getString("discord.prefix", "!");
    }

    public static void setPrefix(String guildId, String prefix) {
        String mode = Main.getInstance().getConfig().getString("discord.prefix-storage", "yaml");

        if (mode.equalsIgnoreCase("yaml")) {
            Main.getInstance().getConfig().set("discord.prefix", prefix);
            Main.getInstance().saveConfig();
            return;
        }

        try (Connection conn = PrefixDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "REPLACE INTO omniconnect_prefixes (guild_id, prefix) VALUES (?, ?)")) {

            ps.setString(1, guildId);
            ps.setString(2, prefix);
            ps.executeUpdate();

        } catch (Exception ignored) {}
    }

    public static void resetPrefix(String guildId) {
        String mode = Main.getInstance().getConfig().getString("discord.prefix-storage", "yaml");

        if (mode.equalsIgnoreCase("yaml")) {
            return;
        }

        try (Connection conn = PrefixDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM omniconnect_prefixes WHERE guild_id=?")) {

            ps.setString(1, guildId);
            ps.executeUpdate();

        } catch (Exception ignored) {}
    }
}
