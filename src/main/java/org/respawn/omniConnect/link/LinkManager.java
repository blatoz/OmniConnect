package org.respawn.omniConnect.link;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public class LinkManager {

    private static LinkManager instance;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private HashMap<String, String> linkedAccounts = new HashMap<>(); // mcUUID → discordId
    private HashMap<String, String> pendingCodes = new HashMap<>();   // code → discordId

    public static LinkManager getInstance() {
        if (instance == null) instance = new LinkManager();
        return instance;
    }

    public void load(File dataFolder) {
        try {
            File file = new File(dataFolder, "links.json");
            if (!file.exists()) {
                save(dataFolder);
                return;
            }

            LinkManager loaded = gson.fromJson(new FileReader(file), LinkManager.class);
            this.linkedAccounts = loaded.linkedAccounts;
            this.pendingCodes = loaded.pendingCodes;

        } catch (Exception e) {
            Bukkit.getLogger().warning("Nem sikerült betölteni a links.json-t!");
        }
    }

    public void save(File dataFolder) {
        try {
            File file = new File(dataFolder, "links.json");
            FileWriter writer = new FileWriter(file);
            gson.toJson(this, writer);
            writer.close();
        } catch (Exception e) {
            Bukkit.getLogger().warning("Nem sikerült menteni a links.json-t!");
        }
    }

    public void addPendingCode(String code, String discordId) {
        pendingCodes.put(code, discordId);
    }

    public String consumeCode(String code) {
        return pendingCodes.remove(code);
    }

    public void link(String mcUUID, String discordId) {
        linkedAccounts.put(mcUUID, discordId);
    }

    public void unlink(String mcUUID) {
        linkedAccounts.remove(mcUUID);
    }

    public boolean isLinked(String mcUUID) {
        return linkedAccounts.containsKey(mcUUID);
    }

    public String getDiscordId(String mcUUID) {
        return linkedAccounts.get(mcUUID);
    }

    public void addPending(String code, String discordId) {
    }
}
