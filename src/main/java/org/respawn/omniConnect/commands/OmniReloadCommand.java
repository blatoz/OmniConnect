package org.respawn.omniConnect.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.DiscordManager;
import org.respawn.omniConnect.config.OmniConfig;
import org.respawn.omniConnect.lang.LangManager;
import org.respawn.omniConnect.hooks.DiscordLog;

public class OmniReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String lang = LangManager.getDefaultLanguage();

        Main.getInstance().reloadConfig();
        LangManager.reload();
        OmniConfig.reload();

        DiscordManager.getInstance().shutdown();
        DiscordManager.getInstance().start();

        sender.sendMessage(LangManager.get(lang, "commands.reload.success"));

        DiscordLog.sendCategory(
                "discord.channels.log",
                LangManager.get(lang, "discord.reload.title"),
                LangManager.get(lang, "discord.reload.description")
        );

        return true;
    }
}
