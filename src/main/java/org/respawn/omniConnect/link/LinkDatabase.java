package org.respawn.omniConnect.link;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.respawn.omniConnect.Main;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class LinkDatabase {

    private static File file;
    private static FileConfiguration cfg;

    public static void init() {
        file = new File(Main.getInstance().getDataFolder(), "links.yml");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException ignored) {}
        }
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    public static void save() {
        try {
            cfg.save(file);
        } catch (IOException ignored) {}
    }

    public static void link(UUID uuid, String discordId) {
        cfg.set("by-uuid." + uuid.toString(), discordId);
        cfg.set("by-discord." + discordId, uuid.toString());
        save();
    }

    public static UUID getMinecraftUUID(String discordId) {
        String s = cfg.getString("by-discord." + discordId);
        if (s == null) return null;
        try {
            return UUID.fromString(s);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static String getDiscordId(UUID uuid) {
        return cfg.getString("by-uuid." + uuid.toString());
    }
}
