package org.respawn.omniConnect.database;

import org.respawn.omniConnect.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PrefixDatabase {

    private static Connection connection;

    public static void init() {
        try {
            String host = Main.getInstance().getConfig().getString("linking.mysql.host");
            int port = Main.getInstance().getConfig().getInt("linking.mysql.port");
            String database = Main.getInstance().getConfig().getString("linking.mysql.database");
            String username = Main.getInstance().getConfig().getString("linking.mysql.username");
            String password = Main.getInstance().getConfig().getString("linking.mysql.password");

            String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&autoReconnect=true";

            connection = DriverManager.getConnection(url, username, password);

            createTable();

        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Failed to initialize PrefixDatabase: " + e.getMessage());
        }
    }

    private static void createTable() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS omniconnect_prefixes (
                    guild_id VARCHAR(32) PRIMARY KEY,
                    prefix VARCHAR(16) NOT NULL
                );
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.executeUpdate();
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
