package org.respawn.omniConnect;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;
import org.respawn.omniConnect.discord.DiscordMessageListener;
import org.respawn.omniConnect.ticket.TicketListener;
import org.respawn.omniConnect.ticket.TicketPanelCommand;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.time.Instant;
import java.util.EnumSet;

/**
 * Discord bot kezelő - singleton minta alapján.
 * Kezeli a bot indítását, leállítását és az üzenetküldenést.
 */
public class DiscordManager {

    private static DiscordManager instance;
    private JDA jda;

    /**
     * Singleton getInstance metódus.
     *
     * @return DiscordManager singleton instancia
     */
    public static DiscordManager getInstance() {
        if (instance == null) {
            instance = new DiscordManager();
        }
        return instance;
    }

    /**
     * Discord bot indítása a config.yml-ben megadott tokennel.
     * Regisztrálja az összes szükséges event listenert.
     */
    public void start() {
        String token = Main.getInstance().getConfig().getString("discord.bot-token");
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
                            new DiscordMessageListener()
                    )
                    .build();

        } catch (LoginException e) {
            Main.getInstance().getLogger().severe("Discord bot indítási hiba: " + e.getMessage());
        }
    }

    /**
     * Discord bot graceful leállítása.
     */
    public void shutdown() {
        if (jda != null) {
            jda.shutdown();
        }
    }

    /**
     * A JDA instancia lekérése.
     *
     * @return JDA instancia vagy null, ha nincs inicializálva
     */
    public JDA getJDA() {
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
        if (jda == null) return;

        String channelId = Main.getInstance().getConfig().getString("discord.chat.channel-id");
        TextChannel channel = jda.getTextChannelById(channelId);
        if (channel == null) return;

        String avatarUrl = "https://mc-heads.net/avatar/" + playerName;

        String displayName = rank.isEmpty()
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
    }
}