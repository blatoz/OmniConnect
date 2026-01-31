package org.respawn.omniConnect.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.respawn.omniConnect.lang.LangManager;

public class OmniConnectHelpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String lang = LangManager.getDefaultLanguage();

        sender.sendMessage(ChatColor.DARK_AQUA + " ");
        sender.sendMessage(ChatColor.DARK_AQUA + "==== " + ChatColor.AQUA + "OmniConnect Help" + ChatColor.DARK_AQUA + " ====");
        sender.sendMessage(ChatColor.GRAY + LangManager.get(lang, "commands.help.description"));
        sender.sendMessage(" ");

        sender.sendMessage(ChatColor.AQUA + "/omnireload " + ChatColor.GRAY + " - " + LangManager.get(lang, "commands.help.reload"));
        sender.sendMessage(ChatColor.AQUA + "/omnirestartbot " + ChatColor.GRAY + " - " + LangManager.get(lang, "commands.help.restartbot"));
        sender.sendMessage(ChatColor.AQUA + "/discordstatus " + ChatColor.GRAY + " - " + LangManager.get(lang, "commands.help.status"));

        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.DARK_AQUA + "==============================");
        sender.sendMessage(" ");

        return true;
    }
}
