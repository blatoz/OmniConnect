package org.respawn.omniConnect.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.respawn.omniConnect.link.LinkManager;

public class LinkCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Ezt a parancsot csak játékos használhatja.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage("Használat: /link <kód>");
            return true;
        }

        String code = args[0];
        String discordId = LinkManager.getInstance().consumeCode(code);

        if (discordId == null) {
            player.sendMessage("§cÉrvénytelen vagy lejárt kód.");
            return true;
        }

        LinkManager.getInstance().link(player.getUniqueId().toString(), discordId);
        player.sendMessage("§aSikeresen összekötötted a Discord fiókodat!");

        return true;
    }
}
