package org.respawn.omniConnect.commands;

import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.DiscordManager;
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

        String uuid = player.getUniqueId().toString();
        String discordId = LinkManager.getInstance().getDiscordId(uuid);

        LinkManager.getInstance().unlink(uuid);
        player.sendMessage("§aDiscord fiók leválasztva!");

// 🔥 LOG Discordra
        TextChannel log = DiscordManager.getInstance().getJDA()
                .getTextChannelById(Main.getInstance().getConfig().getString("discord.link.log-channel-id"));

        if (log != null) {
            log.sendMessage("❌ **Fiók leválasztva**\n"
                    + "Minecraft: **" + player.getName() + "** (`" + uuid + "`)\n"
                    + "Discord ID: `" + discordId + "`").queue();
        }
        return true;
    }
}
