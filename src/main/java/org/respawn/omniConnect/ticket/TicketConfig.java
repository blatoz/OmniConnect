package org.respawn.omniConnect.ticket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class TicketConfig {

    private static TicketConfig instance;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private String staffRoleId = "";

    public static TicketConfig getInstance() {
        if (instance == null) {
            instance = new TicketConfig();
        }
        return instance;
    }

    public void load(File dataFolder) {
        try {
            File file = new File(dataFolder, "ticketconfig.json");
            if (!file.exists()) {
                save(dataFolder);
                return;
            }

            TicketConfig loaded = gson.fromJson(new FileReader(file), TicketConfig.class);
            this.staffRoleId = loaded.staffRoleId;

        } catch (Exception e) {
            Bukkit.getLogger().warning("Nem sikerült betölteni a ticketconfig.json-t!");
        }
    }

    public void save(File dataFolder) {
        try {
            File file = new File(dataFolder, "ticketconfig.json");
            FileWriter writer = new FileWriter(file);
            gson.toJson(this, writer);
            writer.close();
        } catch (Exception e) {
            Bukkit.getLogger().warning("Nem sikerült menteni a ticketconfig.json-t!");
        }
    }

    public String getStaffRoleId() {
        return staffRoleId;
    }

    public void setStaffRoleId(String staffRoleId) {
        this.staffRoleId = staffRoleId;
    }
}
