package org.respawn.omniConnect.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.respawn.omniConnect.hooks.DiscordLog;

public class LinkCommands implements CommandExecutor {

    private final String pluginKey = "links";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        switch (cmd.getName().toLowerCase()) {

            case "discord":
                p.sendMessage("§bDiscord szerverünk: §fhttps://discord.gg/VALAMI");
                DiscordLog.send(pluginKey, "🔗 /discord parancs", "Játékos: **" + p.getName() + "**");
                break;

            case "store":
                p.sendMessage("§bWebshop: §fhttps://store.valami.hu");
                DiscordLog.send(pluginKey, "🔗 /store parancs", "Játékos: **" + p.getName() + "**");
                break;

            case "rules":
                p.sendMessage("§bSzabályzat: §fhttps://valami.hu/rules");
                DiscordLog.send(pluginKey, "🔗 /rules parancs", "Játékos: **" + p.getName() + "**");
                break;

            case "website":
                p.sendMessage("§bWeboldal: §fhttps://valami.hu");
                DiscordLog.send(pluginKey, "🔗 /website parancs", "Játékos: **" + p.getName() + "**");
                break;

            case "vote":
                p.sendMessage("§bSzavazás: §fhttps://valami.hu/vote");
                DiscordLog.send(pluginKey, "🔗 /vote parancs", "Játékos: **" + p.getName() + "**");
                break;

            case "map":
                p.sendMessage("§bTérkép: §fhttps://map.valami.hu");
                DiscordLog.send(pluginKey, "🔗 /map parancs", "Játékos: **" + p.getName() + "**");
                break;

            case "wiki":
                p.sendMessage("§bWiki: §fhttps://wiki.valami.hu");
                DiscordLog.send(pluginKey, "🔗 /wiki parancs", "Játékos: **" + p.getName() + "**");
                break;
        }

        return true;
    }
}
