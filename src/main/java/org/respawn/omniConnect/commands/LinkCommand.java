package org.respawn.omniConnect.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.respawn.omniConnect.link.LinkDatabase;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class LinkCommand implements CommandExecutor {

    // egyszerű kód tárolás memóriában (te később DB-be is teheted)
    private static final java.util.Map<String, UUID> pending = new java.util.HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Csak játékosok használhatják.");
            return true;
        }

        Player p = (Player) sender;
        String code = generateCode();

        pending.put(code, p.getUniqueId());
        p.sendMessage("§aDiscord összekötéshez írd be Discordon: §f/link " + code);

        return true;
    }

    public static UUID consumeCode(String code) {
        return pending.remove(code);
    }

    private String generateCode() {
        int n = ThreadLocalRandom.current().nextInt(100000, 999999);
        return String.valueOf(n);
    }
}
