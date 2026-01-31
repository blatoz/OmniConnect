package org.respawn.omniConnect.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.respawn.omniConnect.Main;

public class OmniConfig {

    private static FileConfiguration config;

    /**
     * First load on plugin start
     */
    public static void load() {
        Main plugin = Main.getInstance();
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }

    /**
     * Reload with /omnireload command
     */
    public static void reload() {
        Main plugin = Main.getInstance();
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    public static String getString(String path) {
        return config.getString(path, "");
    }

    public static boolean getBoolean(String path) {
        return config.getBoolean(path, false);
    }

    public static int getInt(String path) {
        return config.getInt(path, 0);
    }

    public static long getLong(String path) {
        return config.getLong(path, 0L);
    }

    public static org.bukkit.configuration.ConfigurationSection getSection(String path) {
        return config.getConfigurationSection(path);
    }
}
