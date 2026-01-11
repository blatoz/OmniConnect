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
                        Commands.slash("warn", "Warnol egy felhasználót")
                                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.USER, "target", "A warnolandó felhasználó", true)
                                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.STRING, "reason", "Indok", true),

                        Commands.slash("mute", "Némít egy felhasználót")
                                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.USER, "target", "A némítandó felhasználó", true)
                                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.STRING, "reason", "Indok", true),

                        Commands.slash("kick", "Kirúg egy felhasználót")
                                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.USER, "target", "A kirúgandó felhasználó", true)
                                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.STRING, "reason", "Indok", true),

                        Commands.slash("ban", "Kitilt egy felhasználót")
                                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.USER, "target", "A kitiltandó felhasználó", true)
                                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.STRING, "reason", "Indok", true),

                        Commands.slash("timeout", "Időkorlátot ad egy felhasználónak")
                                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.USER, "target", "A felhasználó", true)
                                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER, "minutes", "Percben", true)
                                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.STRING, "reason", "Indok", true),
                        Commands.slash("link", "Minecraft fiók összekötése")
                                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.STRING, "code", "A Minecraft szerveren kapott kód", true),
                        Commands.slash("ping", "Megmutatja a bot késleltetését"),

                        Commands.slash("avatar", "Megmutatja egy felhasználó avatarját")
                                .addOption(OptionType.USER, "user", "A célzott felhasználó", false),

                        Commands.slash("userinfo", "Információk egy felhasználóról")
                                .addOption(OptionType.USER, "user", "A célzott felhasználó", false),

                        Commands.slash("serverinfo", "Információk a Discord szerverről"),

                        Commands.slash("mclink", "Megmutatja, milyen Minecraft fiókkal van összekötve")
                                .addOption(OptionType.USER, "user", "A célzott felhasználó", false),

                        Commands.slash("mcstatus", "Minecraft szerver státusz lekérése"),

                        Commands.slash("roll", "Dob egy véletlen számot")
                                .addOption(OptionType.INTEGER, "max", "Maximum érték (alapértelmezett: 100)", false),

                        Commands.slash("coinflip", "Fej vagy írás dobása"),

                        Commands.slash("8ball", "Magic 8-ball válasz"),

                        Commands.slash("joke", "Random vicc"),

                        Commands.slash("cat", "Random cica kép"),

                        Commands.slash("dog", "Random kutya kép"),

                        Commands.slash("hug", "Megölelsz valakit")
                                .addOption(OptionType.USER, "user", "A megölelt felhasználó", false),

                        Commands.slash("slap", "Megcsapsz valakit")
                                .addOption(OptionType.USER, "user", "A megcsapott felhasználó", false),

                        Commands.slash("meme", "Random meme kép"),

                        Commands.slash("gif", "GIF keresése előre definiált kategóriákból")
                                .addOption(OptionType.STRING, "query", "GIF kategória (pl. cat, dog, happy, sad)", true),

                        Commands.slash("quote", "Random idézet"),

                        Commands.slash("ship", "Két felhasználó kompatibilitása")
                                .addOption(OptionType.USER, "user1", "Első felhasználó", true)
                                .addOption(OptionType.USER, "user2", "Második felhasználó", true),

                        Commands.slash("lovecalc", "Szerelmi kompatibilitás számítása")
                                .addOption(OptionType.USER, "user1", "Első felhasználó", true)
                                .addOption(OptionType.USER, "user2", "Második felhasználó", true)


                        )
                .queue();
    }

    public static JDA getJda() {
        return jda;
    }
}
