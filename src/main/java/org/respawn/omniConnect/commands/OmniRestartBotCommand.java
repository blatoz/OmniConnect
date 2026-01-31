package org.respawn.omniConnect.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.respawn.omniConnect.DiscordManager;
import org.respawn.omniConnect.lang.LangManager;
import org.respawn.omniConnect.hooks.DiscordLog;

public class OmniRestartBotCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String lang = LangManager.getDefaultLanguage();

        DiscordManager.getInstance().shutdown();
        DiscordManager.getInstance().start();

        sender.sendMessage(LangManager.get(lang, "commands.bot.restart"));

        DiscordLog.sendCategory(
                "discord.channels.log",
                LangManager.get(lang, "discord.bot.restart_title"),
                LangManager.get(lang, "discord.bot.restart_description")
        );

        return true;
    }
}
