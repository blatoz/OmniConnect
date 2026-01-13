package org.respawn.omniConnect.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.commands.*;

public class DiscordBotManager {

    private static JDA jda;

    public static void start(String token) throws Exception {
        jda = JDABuilder.createDefault(token)
                .addEventListeners(
                        new org.respawn.omniConnect.commands.DiscordModerationCommands(),
                        new DiscordLinkVerifyListener(),
                        new DiscordFunCommands(),
                        new DiscordUtilsCommands()
                )
                .build()
                .awaitReady();

        registerSlashCommands();
    }

    private static void registerSlashCommands() {
        jda.updateCommands()
                .addCommands(
                        Commands.slash("warn", "Warn an user")
                                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.USER, "target", "Warnebble user", true)
                                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.STRING, "reason", "Reason", true),

                        Commands.slash("mute", "Mute an user")
                                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.USER, "target", "Muteble user", true)
                                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.STRING, "reason", "Reason", true),

                        Commands.slash("kick", "Kick an user")
                                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.USER, "target", "Kickebble user", true)
                                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.STRING, "reason", "Reason", true),

                        Commands.slash("ban", "Ban an user")
                                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.USER, "target", "Bannble user", true)
                                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.STRING, "reason", "Reason", true),

                        Commands.slash("timeout", "Timeout an user")
                                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.USER, "target", "User", true)
                                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER, "minutes", "Minutes", true)
                                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.STRING, "reason", "Reason", true),
                        Commands.slash("link", "Minecraft account link")
                                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.STRING, "code", "Minecraft server code", true),
                        Commands.slash("ping", "Shows the bot's latency"),

                        Commands.slash("avatar", "Shows the user's avatar")
                                .addOption(OptionType.USER, "user", "Target", false),

                        Commands.slash("userinfo", "User information")
                                .addOption(OptionType.USER, "user", "Target", false),

                        Commands.slash("serverinfo", "Server information"),

                        Commands.slash("mclink", "Shows your Minecraft account link or another user's link")
                                .addOption(OptionType.USER, "user", "Target", false),

                        Commands.slash("mcstatus", "Minecraft server status"),

                        Commands.slash("roll", "Roll a random number")
                                .addOption(OptionType.INTEGER, "max", "Maximum value (default: 100)", false),

                        Commands.slash("coinflip", "Heads or tails toss"),

                        Commands.slash("8ball", "Magic 8-ball awnser"),

                        Commands.slash("joke", "Random joke"),

                        Commands.slash("cat", "Random cat picture"),

                        Commands.slash("dog", "Random dog picture"),

                        Commands.slash("hug", "You hug someone")
                                .addOption(OptionType.USER, "user", "The user you want to hug", false),

                        Commands.slash("slap", "You slap someone")
                                .addOption(OptionType.USER, "user", "The user you want to slap", false),

                        Commands.slash("meme", "Random meme picture"),

                        Commands.slash("gif", "Search for a GIF")
                                .addOption(OptionType.STRING, "query", "GIF kategória (etc. cat, dog, happy, sad)", true),

                        Commands.slash("quote", "Random quote"),

                        Commands.slash("ship", "2 user ship")
                                .addOption(OptionType.USER, "user1", "First user", true)
                                .addOption(OptionType.USER, "user2", "Second user", true),

                        Commands.slash("lovecalc", "Love calculator between 2 users")
                                .addOption(OptionType.USER, "user1", "First user", true)
                                .addOption(OptionType.USER, "user2", "Second user", true)


                        )
                .queue();
    }

    public static JDA getJda() {
        return jda;
    }
}
