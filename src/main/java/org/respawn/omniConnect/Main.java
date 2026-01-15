package org.respawn.omniConnect;

import org.bukkit.plugin.java.JavaPlugin;
import org.respawn.omniConnect.discord.DiscordBotManager;
import org.respawn.omniConnect.hooks.HookManager;
import org.respawn.omniConnect.lang.LangManager;
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

                String lang = LangManager.getDefaultLanguage();

                LogManager.getInstance().sendEmbed(embed -> embed
                        .setAuthor(botName, null, botAvatar)
                        .setTitle(LangManager.get(lang, "discord.system.start.title"))
                        .setColor(Color.GREEN)
                        .addField(
                                LangManager.get(lang, "discord.system.field_status"),
                                LangManager.get(lang, "discord.system.start.status"),
                                false
                        )
                );

            } catch (Exception e) {
                getLogger().warning("Failed to send the start embed: " + e.getMessage());
            }
        } else {
            getLogger().warning("The Discord bot is not available, the start message is not sended.");
        }
    }

    @Override
    public void onDisable() {
        // Log Discordra leálláskor (csak ha a bot még elérhető)
        if (DiscordManager.ready) {
            try {
                String botName = DiscordManager.getInstance().getJDA().getSelfUser().getName();
                String botAvatar = DiscordManager.getInstance().getJDA().getSelfUser().getAvatarUrl();

                String lang = LangManager.getDefaultLanguage();

                LogManager.getInstance().sendEmbed(embed -> embed
                        .setAuthor(botName, null, botAvatar)
                        .setTitle(LangManager.get(lang, "discord.system.stop.title"))
                        .setColor(Color.RED)
                        .addField(
                                LangManager.get(lang, "discord.system.field_status"),
                                LangManager.get(lang, "discord.system.stop.status"),
                                false
                        )
                );

            } catch (Exception e) {
                getLogger().warning("Failed to send the shutdown message: " + e.getMessage());
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
