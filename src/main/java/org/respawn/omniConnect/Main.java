package org.respawn.omniConnect;

import org.bukkit.plugin.java.JavaPlugin;
import org.respawn.omniConnect.commands.*;
import org.respawn.omniConnect.config.OmniConfig;
import org.respawn.omniConnect.database.PrefixDatabase;
import org.respawn.omniConnect.discord.PrefixManager;
import org.respawn.omniConnect.hooks.HookManager;
import org.respawn.omniConnect.lang.LangManager;
import org.respawn.omniConnect.listeners.ChatListener;
import org.respawn.omniConnect.ticket.TicketManager;

import org.respawn.omniConnect.discord.DiscordLinkVerifyListener;
import org.respawn.omniConnect.link.LinkDatabase;

import java.awt.*;

/**
 * OmniConnect - Minecraft and Discord synchronization plugin.
 * Handles chat integration, ticket system, and server events.
 */
public class Main extends JavaPlugin {

    private static Main instance;

    /**
     * Retrieving the Main singleton instance.
     *
     * @return Main instance
     */
    public static Main getInstance() {
        return instance;
    }

    /**
     * Initialize and start the plugin.
     * Read the config, register the event listeners, and start the Discord bot.
     */
    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        LangManager.load();
        OmniConfig.load();
        getLogger().info("OmniConnect plugin has been started!");

        // Commands, Listeners
        registerCommands();
        registerListeners();

        // --- Ticket system Initializing from the config  ---
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

        // --- Account linking database ---
        LinkDatabase.init();

        // --- Prefix database ---
        PrefixDatabase.init();


        // --- Hooks Initializing ---
        HookManager.init();
        HookManager.initExploitFixHooks();
        HookManager.initKingdomsHooks();
        HookManager.initModerationHooks();
        HookManager.initManagementHooks();
        HookManager.initEconomyHooks();

        // --- Discord prefix load ---
        PrefixManager.load();


        // --- Discord bot start ---
        DiscordManager.getInstance().start();

        // --- Discord slash command listerek (moderáció + linking) ---
        if (DiscordManager.ready) {
            try {
                DiscordManager.getInstance().getJDA().addEventListener(
                        new DiscordModerationCommands(),
                        new DiscordFunCommands(),
                        new DiscordFunCommands(),
                        new DiscordLinkVerifyListener()
                );

                // Log Discord started embed
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
        // Log Discord on shutdown (only if the bot is ready)
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
        // Minecraft side linking
        if (getCommand("link") != null) {
            getCommand("link").setExecutor(new LinkCommand());
        }

        // Link commands (Discord / Store / Rules / Website / Vote / Map / Wiki)
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
        if (getCommand("omnireload") != null) {
            getCommand("omnireload").setExecutor(new OmniReloadCommand());
        }
        if (getCommand("omnirestartbot") != null) {
            getCommand("omnirestartbot").setExecutor(new OmniRestartBotCommand());
        }
        if (getCommand("discordstatus") != null) {
            getCommand("discordstatus").setExecutor(new DiscordStatusCommand());
        }
        if (getCommand("omniconnect") != null) {
            getCommand("omnicomnect").setExecutor(new OmniConnectHelpCommand());
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }
}
