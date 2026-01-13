package org.respawn.omniConnect.lang;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.respawn.omniConnect.Main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LangManager {

    private static final Map<String, org.bukkit.configuration.file.YamlConfiguration> LANGUAGES = new HashMap<>();

    private static String defaultLanguage = "en";

    public static void load() {
        Main plugin = Main.getInstance();

        defaultLanguage = plugin.getConfig().getString("language", "en");

        File langFolder = new File(plugin.getDataFolder(), "lang");
        if (!langFolder.exists()) langFolder.mkdirs();

        // Betöltjük az összes .yml nyelvi fájlt
        File[] files = langFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".yml")) {
                    String lang = file.getName().replace(".yml", "");
                    LANGUAGES.put(lang, YamlConfiguration.loadConfiguration(file));
                }
            }
        }

        // Ha nincs angol → létrehozzuk
        if (!LANGUAGES.containsKey("en")) {
            plugin.saveResource("lang/en.yml", false);
            LANGUAGES.put("en", YamlConfiguration.loadConfiguration(new File(langFolder, "en.yml")));
        }
    }
    public static boolean languageExists(String lang) {
        if (lang == null) return false;
        return LANGUAGES.containsKey(lang.toLowerCase());
    }


    public static boolean hasLanguage(String lang) {
        return LANGUAGES.containsKey(lang);
    }

    public static String getDefaultLanguage() {
        return defaultLanguage;
    }

    public static String get(String lang, String key) {
        FileConfiguration cfg = LANGUAGES.get(lang);

        if (cfg != null && cfg.contains(key)) {
            return cfg.getString(key);
        }

        // fallback angolra
        FileConfiguration en = LANGUAGES.get("en");
        return en.getString(key, "§cMissing lang key: " + key);
    }


    public static String get(String lang, String key, Map<String, String> placeholders) {
        String text = get(lang, key);
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            text = text.replace("%" + entry.getKey() + "%", entry.getValue());
        }
        return text;
    }
}
