package org.respawn.omniConnect.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.respawn.omniConnect.link.LinkManager;

public class UnlinkCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Ezt a parancsot csak játékos használhatja.");
            return true;
        }

        Player player = (Player) sender;

        if (!LinkManager.getInstance().isLinked(player.getUniqueId().toString())) {
            player.sendMessage("§cNincs összekötve Discord fiók.");
            return true;
        }

        LinkManager.getInstance().unlink(player.getUniqueId().toString());
        player.sendMessage("§aDiscord fiók leválasztva.");

        return true;
    }
}
