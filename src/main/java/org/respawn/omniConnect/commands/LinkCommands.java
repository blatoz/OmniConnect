package org.respawn.omniConnect.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class LinkCommands implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        String lang = LangManager.getDefaultLanguage();
        String key = cmd.getName().toLowerCase();

        // Config: links.<command>.enabled
        if (!Main.getInstance().getConfig().getBoolean("links." + key + ".enabled", true)) {
            p.sendMessage(LangManager.get(lang, "minecraft.links.disabled"));
            return true;
        }

        // Config: links.<command>.url
        String url = Main.getInstance().getConfig().getString("links." + key + ".url", "");
        if (url.isEmpty()) {
            p.sendMessage(LangManager.get(lang, "minecraft.links.not_set"));
            return true;
        }

        // Minecraft üzenet
        String msg = LangManager.get(lang, "minecraft.links." + key)
                .replace("%url%", url);
        p.sendMessage(msg);

        // Discord embed log
        String title = LangManager.get(lang, "discord.links." + key + ".title");
        String desc = LangManager.get(lang, "discord.links." + key + ".description")
                .replace("%player%", p.getName())
                .replace("%url%", url);

        DiscordLog.sendCategory(
                "discord.links.log-channel",
                title,
                desc
        );

        return true;
    }
}
