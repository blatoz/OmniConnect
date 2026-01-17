package org.respawn.omniConnect.lang;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.respawn.omniConnect.Main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LangManager {

    private static final Map<String, YamlConfiguration> LANGUAGES = new HashMap<>();
    private static String defaultLanguage = "en";

    public static void load() {
        Main plugin = Main.getInstance();

        defaultLanguage = plugin.getConfig().getString("language", "en");

        File langFolder = new File(plugin.getDataFolder(), "lang");
        if (!langFolder.exists()) langFolder.mkdirs();

        // Automatikus nyelvi fájl másolás, ha hiányzik
        copyLangFile("en.yml");
        copyLangFile("hu.yml");

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
    }

    private static void copyLangFile(String fileName) {
        File outFile = new File(Main.getInstance().getDataFolder() + "/lang", fileName);
        if (!outFile.exists()) {
            Main.getInstance().saveResource("lang/" + fileName, false);
        }
    }

    public static boolean languageExists(String lang) {
        return lang != null && LANGUAGES.containsKey(lang.toLowerCase());
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
