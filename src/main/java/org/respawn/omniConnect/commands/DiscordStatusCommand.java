package org.respawn.omniConnect.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.respawn.omniConnect.DiscordManager;
import org.respawn.omniConnect.lang.LangManager;

public class DiscordStatusCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String lang = LangManager.getDefaultLanguage();

        boolean ready = DiscordManager.ready;

        if (ready) {
            sender.sendMessage(ChatColor.GREEN + LangManager.get(lang, "discord.status.online"));
        } else {
            sender.sendMessage(ChatColor.RED + LangManager.get(lang, "discord.status.offline"));
        }

        return true;
    }
}
