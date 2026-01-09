package org.respawn.omniConnect.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.commands.*;

public class DiscordBotManager {

    private static JDA jda;

    public static void start(String token) throws Exception {
        jda = JDABuilder.createDefault(token)
                .addEventListeners(
                        new org.respawn.omniConnect.discord.DiscordModerationCommands(),
                        new DiscordLinkVerifyListener()
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
                                .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.STRING, "code", "A Minecraft szerveren kapott kód", true)

                )
                .queue();
    }

    public static JDA getJda() {
        return jda;
    }
}
