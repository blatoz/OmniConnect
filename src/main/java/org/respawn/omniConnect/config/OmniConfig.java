package org.respawn.omniConnect.config;

import org.bukkit.configuration.ConfigurationSection;
import org.respawn.omniConnect.Main;

public class OmniConfig {

    private static final Main plugin = Main.getInstance();

    public static String getString(String path) {
        return plugin.getConfig().getString(path, "");
    }

    public static boolean getBoolean(String path) {
        return plugin.getConfig().getBoolean(path, false);
    }

    public static int getInt(String path) {
        return plugin.getConfig().getInt(path, 0);
    }

    public static long getLong(String path) {
        return plugin.getConfig().getLong(path, 0L);
    }

    public static ConfigurationSection getSection(String path) {
        return plugin.getConfig().getConfigurationSection(path);
    }
}
