package org.respawn.omniConnect;

import org.bukkit.plugin.java.JavaPlugin;
import org.respawn.omniConnect.listeners.ChatListener;
import org.respawn.omniConnect.ticket.TicketManager;
import org.respawn.omniConnect.ticket.TicketConfig;

import java.awt.*;


/**
 * OmniConnect - Minecraft és Discord szinkronizációs plugin.
 * Kezeli a chat integrációt, jegyrendszert és szerv eseményeit.
 */
public class Main extends JavaPlugin {

    private static Main instance;

    /**
     * A Main singleton instancia lekérése.
     *
     * @return Main instancia
     */
    public static Main getInstance() {
        return instance;
    }

    /**
     * Plugin inicializálása és indítása.
     * Beolvassa a config-ot, regisztrálja az eseménylisszenereket és elindítja a Discord botot.
     */
    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        getLogger().info("OmniConnect plugin elindult!");

        registerCommands();
        registerListeners();

        // --- Ticket rendszer inicializálása configból ---
        String guildId = getConfig().getString("discord.guild-id");
        String ticketCategoryId = getConfig().getString("discord.ticket.category-id");
        String supportRoleId = getConfig().getString("discord.ticket.support-role-id");
        String logChannelId = getConfig().getString("discord.ticket.log-channel-id");
        String panelChannelId = getConfig().getString("discord.ticket.panel-channel-id");

        TicketManager.init(
                guildId,
                ticketCategoryId,
                supportRoleId,
                logChannelId,
                panelChannelId
        );

        // 🔥 TicketConfig betöltése
        TicketConfig.getInstance().load(getDataFolder());

        // Discord bot indítása
        DiscordManager.getInstance().start();

        // Log Discordra induláskor
        LogManager.getInstance().sendEmbed(embed -> embed
                .setTitle("Szerver elindult")
                .setColor(Color.GREEN)
                .addField("Státusz", "A Minecraft szerver elindult és az OmniConnect aktív.", false)
        );
    }

    @Override
    public void onDisable() {

        // 🔥 TicketConfig mentése
        TicketConfig.getInstance().save(getDataFolder());

        // Log Discordra leálláskor
        LogManager.getInstance().sendEmbed(embed -> embed
                .setTitle("Szerver leáll")
                .setColor(Color.RED)
                .addField("Státusz", "A Minecraft szerver leállt vagy újraindul.", false)
        );

        DiscordManager.getInstance().shutdown();
    }

    private void registerCommands() {
        // Nem lesz még használva
    }


    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }
}
