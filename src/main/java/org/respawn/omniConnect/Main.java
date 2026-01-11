package org.respawn.omniConnect;

import org.bukkit.plugin.java.JavaPlugin;
import org.respawn.omniConnect.hooks.HookManager;
import org.respawn.omniConnect.listeners.ChatListener;
import org.respawn.omniConnect.ticket.TicketManager;

import org.respawn.omniConnect.commands.LinkCommand;
import org.respawn.omniConnect.commands.LinkCommands;
import org.respawn.omniConnect.commands.DiscordModerationCommands;
import org.respawn.omniConnect.discord.DiscordLinkVerifyListener;
import org.respawn.omniConnect.link.LinkDatabase;

import java.awt.*;

/**
 * OmniConnect - Minecraft és Discord szinkronizációs plugin.
 * Kezeli a chat integrációt, jegyrendszert és szerver eseményeit.
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
     * Beolvassa a config-ot, regisztrálja az eseménylistenereket és elindítja a Discord botot.
     */
    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        getLogger().info("OmniConnect plugin elindult!");

        // Parancsok, listenerek
        registerCommands();
        registerListeners();

        // --- Ticket rendszer inicializálása configból ---
        String guildId = getConfig().getString("discord.guild-id");
        String ticketCategoryId = getConfig().getString("discord.ticket.category-id");
        String logChannelId = getConfig().getString("discord.ticket.log-channel-id");
        String panelChannelId = getConfig().getString("discord.ticket.panel-channel-id");

        TicketManager.init(
                guildId,
                ticketCategoryId,
                logChannelId,
                panelChannelId
        );

        // --- Account linking adatbázis ---
        LinkDatabase.init();

        // --- Hookok inicializálása ---
        HookManager.init();
        HookManager.initExploitFixHooks();
        HookManager.initKingdomsHooks();
        HookManager.initModerationHooks();
        HookManager.initManagementHooks();
        HookManager.initEconomyHooks();

        // --- Discord bot indítása ---
        DiscordManager.getInstance().start();

        // --- Discord slash command listenerek (moderáció + linking) ---
        if (DiscordManager.ready) {
            try {
                DiscordManager.getInstance().getJDA().addEventListener(
                        new DiscordModerationCommands(),
                        new DiscordLinkVerifyListener()
                );

                // Log Discordra induláskor
                String botName = DiscordManager.getInstance().getJDA().getSelfUser().getName();
                String botAvatar = DiscordManager.getInstance().getJDA().getSelfUser().getAvatarUrl();

                LogManager.getInstance().sendEmbed(embed -> embed
                        .setAuthor(botName, null, botAvatar)
                        .setTitle("Szerver Elindult")
                        .setColor(Color.GREEN)
                        .addField("Státusz", "A Minecraft szerver elindult és az OmniConnect aktív.\nA plugin sikeresen újratöltött.", false)
                );
            } catch (Exception e) {
                getLogger().warning("Nem sikerült az indulási embed küldése: " + e.getMessage());
            }
        } else {
            getLogger().warning("A Discord bot nem áll rendelkezésre, az indulási üzenet nem került elküldésre.");
        }
    }

    @Override
    public void onDisable() {
        // Log Discordra leálláskor (csak ha a bot még elérhető)
        if (DiscordManager.ready) {
            try {
                String botName = DiscordManager.getInstance().getJDA().getSelfUser().getName();
                String botAvatar = DiscordManager.getInstance().getJDA().getSelfUser().getAvatarUrl();

                LogManager.getInstance().sendEmbed(embed -> embed
                        .setAuthor(botName, null, botAvatar)
                        .setTitle("Szerver Leáll")
                        .setColor(Color.RED)
                        .addField("Státusz", "A Minecraft szerver leállt vagy újraindul.\nVagy a plugint újratöltik.", false)
                );
            } catch (Exception e) {
                getLogger().warning("Nem sikerült a leállási üzenet küldése: " + e.getMessage());
            }
        }

        DiscordManager.getInstance().shutdown();
    }

    private void registerCommands() {
        // Minecraft oldali linking
        if (getCommand("link") != null) {
            getCommand("link").setExecutor(new LinkCommand());
        }

        // Link parancsok (Discord / Store / Rules / Website / Vote / Map / Wiki)
        if (getCommand("discord") != null) {
            getCommand("discord").setExecutor(new LinkCommands());
        }
        if (getCommand("store") != null) {
            getCommand("store").setExecutor(new LinkCommands());
        }
        if (getCommand("rules") != null) {
            getCommand("rules").setExecutor(new LinkCommands());
        }
        if (getCommand("website") != null) {
            getCommand("website").setExecutor(new LinkCommands());
        }
        if (getCommand("vote") != null) {
            getCommand("vote").setExecutor(new LinkCommands());
        }
        if (getCommand("map") != null) {
            getCommand("map").setExecutor(new LinkCommands());
        }
        if (getCommand("wiki") != null) {
            getCommand("wiki").setExecutor(new LinkCommands());
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }
}
