package org.respawn.omniConnect;

import org.respawn.omniConnect.hooks.HookManager;
import org.respawn.omniConnect.listeners.ChatListener;

import org.bukkit.plugin.java.JavaPlugin;
import org.respawn.omniConnect.ticket.TicketManager;

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
        String logChannelId = getConfig().getString("discord.ticket.log-channel-id");
        String panelChannelId = getConfig().getString("discord.ticket.panel-channel-id");

        TicketManager.init(
                guildId,
                ticketCategoryId,
                logChannelId,
                panelChannelId
        );
        HookManager.init();
        HookManager.initExploitFixHooks();
        HookManager.initKingdomsHooks();
        HookManager.initModerationHooks();

        // Discord bot indítása
        DiscordManager.getInstance().start();

        // Log Discordra induláskor (csak ha a bot készen áll)
        if (DiscordManager.ready) {
            try {
                String botName = DiscordManager.getInstance().getJDA().getSelfUser().getName();
                String botAvatar = DiscordManager.getInstance().getJDA().getSelfUser().getAvatarUrl();

                LogManager.getInstance().sendEmbed(embed -> embed
                        .setAuthor(botName, null, botAvatar)
                        .setTitle("Szerver Elindult")
                        .setColor(Color.GREEN)
                        .addField("Státusz", "A Minecraft szerver elindult és az OmniConnect aktív.\nA plugin sikeresen újratöltött.", false)
                );
            } catch (Exception e) {
                getLogger().warning("Nem sikerült az embed küldése: " + e.getMessage());
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
        // getCommand("link").setExecutor(new LinkCommand());
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }
}
