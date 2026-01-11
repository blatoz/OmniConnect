package org.respawn.omniConnect;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;
import org.respawn.omniConnect.commands.DiscordModerationCommands;
import org.respawn.omniConnect.discord.DiscordLinkVerifyListener;
import org.respawn.omniConnect.discord.DiscordMessageListener;
import org.respawn.omniConnect.ticket.TicketListener;
import org.respawn.omniConnect.ticket.TicketPanelCommand;

import javax.security.auth.login.LoginException;
import javax.annotation.Nonnull;
import java.awt.*;
import java.time.Instant;
import java.util.EnumSet;


/**
 * Discord bot kezelő - singleton minta alapján.
 * Kezeli a bot indítását, leállítását és az üzenetküldenést.
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
     * Singleton getInstance metódus.
     *
     * @return DiscordManager singleton instancia
     */
    public static synchronized DiscordManager getInstance() {
        if (instance == null) {
            instance = new DiscordManager();
        }
        return instance;
    }

    /**
     * Indítás alternatív egyszerűsített init metódus (ha más kód használja).
     * Megtartva kompatibilitás kedvéért, de nem publikusan használandó.
     */
    public static void init(String token) {
        getInstance().startWithToken(token);
    }

    // Internal start helper used by init(token)
    private void startWithToken(String token) {
        if (token == null || token.isEmpty()) {
            Bukkit.getLogger().warning("[OmniConnect] startWithToken hívva, de a token üres!");
            return;
        }

        try {
            jda = JDABuilder.createDefault(token)
                    .addEventListeners(new ListenerAdapter() {
                        @Override
                        public void onReady(@Nonnull ReadyEvent event) {
                            ready = true;
                            Bukkit.getLogger().info("[OmniConnect] JDA READY – Discord kapcsolat aktív!");
                        }
                    })
                    .build();

            // Wait until JDA is fully connected and ready
            jda.awaitReady();
            ready = true;

        } catch (LoginException e) {
            Bukkit.getLogger().severe("[OmniConnect] Nem sikerült bejelentkezni a Discord botként: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Bukkit.getLogger().severe("[OmniConnect] JDA awaitReady megszakítva: " + e.getMessage());
        } catch (Exception e) {
            Bukkit.getLogger().severe("[OmniConnect] Nem sikerült inicializálni a JDA-t: " + e.getMessage());
        }
    }

    /**
     * Discord bot indítása a config.yml-ben megadott tokennel.
     * Regisztrálja az összes szükséges event listenert.
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
            Bukkit.getLogger().warning("[OmniConnect] Nincs megadva Discord bot token a config.yml-ben!");
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
                            new DiscordLinkVerifyListener()
                    )
                    .build();

            // Block until JDA is ready so subsequent getJDA() calls won't return null
            jda.awaitReady();
            ready = true;
            Bukkit.getLogger().info("[OmniConnect] Discord bot elindult és készen áll.");

        } catch (LoginException e) {
            Main.getInstance().getLogger().severe("Discord bot indítási hiba: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Main.getInstance().getLogger().severe("Discord bot indítás megszakítva: " + e.getMessage());
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Discord bot indítási kivétel: " + e.getMessage());
        }
    }

    /**
     * Discord bot graceful leállítása.
     */
    public void shutdown() {
        if (jda != null) {
            jda.shutdown();
            jda = null;
            ready = false;
            Bukkit.getLogger().info("[OmniConnect] Discord bot leállt.");
        }
    }

    /**
     * A JDA instancia lekérése.
     *
     * Ez a metódus null-t ad vissza, ha a JDA még nincs inicializálva vagy nincs token.
     *
     * @return JDA instancia vagy null, ha nem elérhető
     */
    public synchronized JDA getJDA() {
        if (jda == null) {
            // Try to initialize from config (support both keys)
            String token = Main.getInstance().getConfig().getString("discord.token");
            if (token == null || token.isEmpty()) {
                token = Main.getInstance().getConfig().getString("discord.bot-token");
            }
            if (token == null || token.isEmpty()) {
                Bukkit.getLogger().warning("[OmniConnect] Discord bot token nincs megadva a config.yml-ben, így a JDA nem inicializálható.");
                return null;
            }

            // Attempt to start synchronously
            start();

            if (jda == null) {
                Bukkit.getLogger().warning("[OmniConnect] A JDA inicializálása sikertelen volt.");
                return null;
            }
        }
        return jda;
    }

    /**
     * Minecraft üzenet küldése a Discord chat csatornára.
     *
     * @param playerName A játékos neve
     * @param message Az üzenet tartalma
     * @param rank A játékos rangja/csoportja
     */
    public void sendMinecraftChat(String playerName, String message, String rank) {
        try {
            JDA currentJda = getJDA();
            if (currentJda == null) {
                return; // Bot nem elérhető, ne küldünk üzenetet
            }

            // Prefer the actual keys from config.yml: discord.chat-bridge.channel-id or discord.channels.main
            String channelId = Main.getInstance().getConfig().getString("discord.chat-bridge.channel-id");
            if (channelId == null || channelId.isEmpty()) {
                channelId = Main.getInstance().getConfig().getString("discord.chat.channel-id");
            }
            if (channelId == null || channelId.isEmpty()) {
                channelId = Main.getInstance().getConfig().getString("discord.channels.main");
            }

            if (channelId == null || channelId.isEmpty()) {
                Bukkit.getLogger().warning("[OmniConnect] Nincs megadva discord.chat csatorna a config.yml-ben!");
                return;
            }

            TextChannel channel = currentJda.getTextChannelById(channelId);
            if (channel == null) {
                Bukkit.getLogger().warning("[OmniConnect] Nem található a megadott Discord csatorna: " + channelId);
                return;
            }

            String avatarUrl = "https://mc-heads.net/avatar/" + playerName;

            String displayName = (rank == null || rank.isEmpty())
                    ? playerName
                    : "[" + rank + "] " + playerName;

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Minecraft Chat")
                    .setColor(Color.GREEN)
                    .setThumbnail(avatarUrl)
                    .addField("Játékos", displayName, true)
                    .addField("Üzenet", message, false)
                    .setTimestamp(Instant.now());

            channel.sendMessageEmbeds(embed.build()).queue();
        } catch (Exception e) {
            Bukkit.getLogger().severe("[OmniConnect] Hiba történt a Discord üzenetküldés során: " + e.getMessage());
        }
    }
}
