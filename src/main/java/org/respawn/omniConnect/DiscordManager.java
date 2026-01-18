package org.respawn.omniConnect;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;
import org.respawn.omniConnect.commands.*;
import org.respawn.omniConnect.discord.DiscordCommandListener;
import org.respawn.omniConnect.discord.DiscordLinkVerifyListener;
import org.respawn.omniConnect.discord.DiscordMessageListener;
import org.respawn.omniConnect.lang.LangManager;
import org.respawn.omniConnect.ticket.TicketListener;
import org.respawn.omniConnect.ticket.TicketPanelCommand;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.OptionType;


import javax.security.auth.login.LoginException;
import javax.annotation.Nonnull;
import java.awt.*;
import java.time.Instant;
import java.util.EnumSet;


/**
 * Discord bot handler - based on the singleton pattern.
 * Handles bot startup, shutdown, and message sending.
 */
public class DiscordManager {

    // Singleton instance
    private static DiscordManager instance;

    // JDA instance
    private JDA jda;
    public static boolean ready = false;

    private DiscordManager() {
        // private ctor for singleton
    }

    /**
     * Singleton getInstance method.
     *
     * @return DiscordManager singleton instance
     */
    public static synchronized DiscordManager getInstance() {
        if (instance == null) {
            instance = new DiscordManager();
        }
        return instance;
    }

    /**
     * Start alternative simplified init method (if used by other code).
     * Retained for compatibility, but not for public use.
     */
    public static void init(String token) {
        getInstance().startWithToken(token);
    }

    // Internal start helper used by init(token)
    private void startWithToken(String token) {
        if (token == null || token.isEmpty()) {
            Bukkit.getLogger().warning("[OmniConnect] startWithToken called, but the token is empty!");
            return;
        }

        try {
            jda = JDABuilder.createDefault(token)
                    .addEventListeners(new ListenerAdapter() {
                        @Override
                        public void onReady(@Nonnull ReadyEvent event) {
                            ready = true;
                            Bukkit.getLogger().info("[OmniConnect] JDA READY – Discord connection is ready!");
                        }
                    })
                    .build();

            // Wait until JDA is fully connected and ready
            jda.awaitReady();
            ready = true;

        } catch (LoginException e) {
            Bukkit.getLogger().severe("[OmniConnect] Could not login in the discord bot: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Bukkit.getLogger().severe("[OmniConnect] JDA awaitReady canceled: " + e.getMessage());
        } catch (Exception e) {
            Bukkit.getLogger().severe("[OmniConnect] JDA Initialization failed: " + e.getMessage());
        }
    }

    /**
     * Start the Discord bot with the token specified in config.yml.
     * Register all necessary event listeners.
     */
    public synchronized void start() {
        // If already started and ready, do nothing
        if (jda != null && ready) return;

        // Support both config keys: discord.token (current config.yml) and discord.bot-token (older code)
        String token = Main.getInstance().getConfig().getString("discord.token");
        if (token == null || token.isEmpty()) {
            token = Main.getInstance().getConfig().getString("discord.bot-token");
        }

        if (token == null || token.isEmpty()) {
            Bukkit.getLogger().warning("[OmniConnect] The discord bot token is not set in the config.yml!");
            return;
        }

        try {
            jda = JDABuilder.createDefault(token)
                    .enableIntents(EnumSet.of(
                            GatewayIntent.GUILD_MESSAGES,
                            GatewayIntent.GUILD_MEMBERS,
                            GatewayIntent.GUILD_VOICE_STATES
                    ))
                    .addEventListeners(
                            new DiscordListener(),
                            new TicketListener(),
                            new TicketPanelCommand(),
                            new DiscordMessageListener(),
                            new DiscordModerationCommands(),
                            new DiscordLinkVerifyListener(),
                            new DiscordCommandListener()
                    )
                    .build();
            // Slash command registration
            registerSlashCommands(jda);


            // Block until JDA is ready so subsequent getJDA() calls won't return null
            jda.awaitReady();
            ready = true;
            Bukkit.getLogger().info("[OmniConnect] Discord bot has been started and ready.");

        } catch (LoginException e) {
            Main.getInstance().getLogger().severe("Discord bot start error: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Main.getInstance().getLogger().severe("Discord bot start canceled: " + e.getMessage());
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Discord bot start exception: " + e.getMessage());
        }
    }

    /**
     * Discord bot graceful shtudown.
     */
    public void shutdown() {
        if (jda != null) {
            jda.shutdown();
            jda = null;
            ready = false;
            Bukkit.getLogger().info("[OmniConnect] Discord bot has been shutdown.");
        }
    }

    /**
     * Retrieving the JDA instance.
     *
     * This method returns null if the JDA has not yet been initialized or there is no token.
     *
     * @return JDA instance or null if not available
     */
    public synchronized JDA getJDA() {
        if (jda == null) {
            // Try to initialize from config (support both keys)
            String token = Main.getInstance().getConfig().getString("discord.token");
            if (token == null || token.isEmpty()) {
                token = Main.getInstance().getConfig().getString("discord.bot-token");
            }
            if (token == null || token.isEmpty()) {
                Bukkit.getLogger().warning("[OmniConnect] Discord bot token not added in config.yml, the JDA cannot be initialized.");
                return null;
            }

            // Attempt to start synchronously
            start();

            if (jda == null) {
                Bukkit.getLogger().warning("[OmniConnect] JDA Initialization failed.");
                return null;
            }
        }
        return jda;
    }


    private void registerSlashCommands(JDA jda) {
        try {
            // Global Commands (All servers can use the command)
            jda.updateCommands()
                    .addCommands(
                            // Moderation
                            Commands.slash("warn", "Warn a user")
                                    .addOption(OptionType.USER, "user", "Target user", true)
                                    .addOption(OptionType.STRING, "reason", "Reason", false),

                            Commands.slash("kick", "Kick a user")
                                    .addOption(OptionType.USER, "user", "Target user", true)
                                    .addOption(OptionType.STRING, "reason", "Reason", false),

                            Commands.slash("ban", "Ban a user")
                                    .addOption(OptionType.USER, "user", "Target user", true)
                                    .addOption(OptionType.STRING, "reason", "Reason", false),

                            Commands.slash("timeout", "Timeout a user")
                                    .addOption(OptionType.USER, "user", "Target user", true)
                                    .addOption(OptionType.INTEGER, "minutes", "Duration in minutes", true),

                            // Utils
                            Commands.slash("ping", "Show bot latency"),
                            Commands.slash("avatar", "Show a user's avatar")
                                    .addOption(OptionType.USER, "user", "Target user", false),
                            Commands.slash("userinfo", "Show user info")
                                    .addOption(OptionType.USER, "user", "Target user", false),
                            Commands.slash("serverinfo", "Show server info"),
                            Commands.slash("mclink", "Show your linked Minecraft account"),
                            Commands.slash("mcstatus", "Show Minecraft server status"),

                            // Fun
                            Commands.slash("roll", "Roll a random number")
                                    .addOption(OptionType.INTEGER, "max", "Maximum number", false),

                            Commands.slash("coinflip", "Flip a coin"),

                            Commands.slash("8ball", "Ask the magic 8-ball"),

                            Commands.slash("joke", "Get a random joke"),

                            Commands.slash("cat", "Random cat image"),
                            Commands.slash("dog", "Random dog image"),

                            Commands.slash("hug", "Hug someone")
                                    .addOption(OptionType.USER, "user", "Target user", true),

                            Commands.slash("slap", "Slap someone")
                                    .addOption(OptionType.USER, "user", "Target user", true),

                            Commands.slash("meme", "Random meme"),

                            Commands.slash("gif", "Search GIFs")
                                    .addOption(OptionType.STRING, "query", "Search term", true),

                            Commands.slash("quote", "Random quote"),

                            Commands.slash("ship", "Ship two users")
                                    .addOption(OptionType.USER, "user1", "First user", true)
                                    .addOption(OptionType.USER, "user2", "Second user", true),

                            Commands.slash("lovecalc", "Love calculator")
                                    .addOption(OptionType.USER, "user1", "First user", true)
                                    .addOption(OptionType.USER, "user2", "Second user", true),
                            Commands.slash("setprefix", "Set the command prefix for this server.")
                                    .addOption(OptionType.STRING, "prefix", "New prefix", true),

                            Commands.slash("resetprefix", "Reset the prefix to default."),

                            Commands.slash("prefix", "Show the current prefix.")


                    )
                    .queue();

            Bukkit.getLogger().info("[OmniConnect] Slash commands has been registered.");

        } catch (Exception e) {
            Bukkit.getLogger().severe("[OmniConnect] Slash command register error: " + e.getMessage());
        }
    }


    /**
     * Send a Minecraft message to the Discord chat channel.
     *
     * @param playerName The player's name
     * @param message The content of the message
     * @param rank The player's rank/group
     */
    public void sendMinecraftChat(String playerName, String message, String rank) {
        try {
            JDA currentJda = getJDA();
            if (currentJda == null) {
                return; // Bot not available
            }

            String channelId = Main.getInstance().getConfig().getString("discord.chat-bridge.channel-id");
            if (channelId == null || channelId.isEmpty()) {
                channelId = Main.getInstance().getConfig().getString("discord.chat.channel-id");
            }
            if (channelId == null || channelId.isEmpty()) {
                channelId = Main.getInstance().getConfig().getString("discord.channels.main");
            }

            if (channelId == null || channelId.isEmpty()) {
                Bukkit.getLogger().warning("[OmniConnect] The config discord.chat channel ID is not added in the config.yml!");
                return;
            }

            TextChannel channel = currentJda.getTextChannelById(channelId);
            if (channel == null) {
                Bukkit.getLogger().warning("[OmniConnect] The channel is not found with this ID: " + channelId);
                return;
            }

            String lang = LangManager.getDefaultLanguage();

            String avatarUrl = "https://mc-heads.net/avatar/" + playerName;

            String displayName = (rank == null || rank.isEmpty())
                    ? playerName
                    : "[" + rank + "] " + playerName;

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(LangManager.get(lang, "discord.chatbridge.title"))
                    .setColor(Color.GREEN)
                    .setThumbnail(avatarUrl)
                    .addField(
                            LangManager.get(lang, "discord.chatbridge.player"),
                            displayName,
                            true
                    )
                    .addField(
                            LangManager.get(lang, "discord.chatbridge.message"),
                            message,
                            false
                    )
                    .setTimestamp(Instant.now());

            channel.sendMessageEmbeds(embed.build()).queue();

        } catch (Exception e) {
            Bukkit.getLogger().severe("[OmniConnect] Error occured when tried send the minecraft chat message: " + e.getMessage());
        }
    }
}

