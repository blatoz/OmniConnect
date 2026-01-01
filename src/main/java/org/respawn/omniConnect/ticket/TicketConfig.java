package org.respawn.omniConnect.ticket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.respawn.omniConnect.Main;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TicketConfig {

    private static TicketConfig instance;

    private String guildId = "";
    private String ticketCategoryId = "";
    private String staffRoleId = "";
    private String logChannelId = "";
    private String panelChannelId = "";

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static File file;

    public static void load() {
        file = new File(Main.getInstance().getDataFolder(), "ticketconfig.json");

        if (!file.exists()) {
            saveDefault();
        }

        try (FileReader reader = new FileReader(file)) {
            instance = gson.fromJson(reader, TicketConfig.class);
        } catch (Exception e) {
            e.printStackTrace();
            saveDefault();
        }
    }

    private static void saveDefault() {
        instance = new TicketConfig();
        instance.save();
    }

    public void save() {
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TicketConfig getInstance() {
        return instance;
    }

    public String getGuildId() {
        return guildId;
    }

    public String getTicketCategoryId() {
        return ticketCategoryId;
    }

    public String getStaffRoleId() {
        return staffRoleId;
    }

    public String getLogChannelId() {
        return logChannelId;
    }

    public String getPanelChannelId() {
        return panelChannelId;
    }

    // Setterek, ha később szerkeszteni akarod a JSON-t
    public void setGuildId(String guildId) {
        this.guildId = guildId;
        save();
    }

    public void setTicketCategoryId(String ticketCategoryId) {
        this.ticketCategoryId = ticketCategoryId;
        save();
    }

    public void setStaffRoleId(String staffRoleId) {
        this.staffRoleId = staffRoleId;
        save();
    }

    public void setLogChannelId(String logChannelId) {
        this.logChannelId = logChannelId;
        save();
    }

    public void setPanelChannelId(String panelChannelId) {
        this.panelChannelId = panelChannelId;
        save();
    }
}
