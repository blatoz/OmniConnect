package org.respawn.omniConnect.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;
import org.respawn.omniConnect.link.LinkDatabase;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class LinkCommand implements CommandExecutor {

    private static final Map<String, UUID> pending = new ConcurrentHashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String lang = LangManager.getDefaultLanguage();

        if (!(sender instanceof Player)) {
            sender.sendMessage(LangManager.get(lang, "minecraft.link.only_players"));
            return true;
        }


        Player p = (Player) sender;


        // Config: link.enabled
        if (!Main.getInstance().getConfig().getBoolean("link.enabled", true)) {
            p.sendMessage(LangManager.get(lang, "minecraft.link.disabled"));
            return true;
        }

        // Config: link.message-key
        String msgKey = Main.getInstance().getConfig().getString(
                "link.message-key",
                "minecraft.link.generated"
        );

        String alreadyKey = Main.getInstance().getConfig().getString(
                "link.already-key",
                "minecraft.link.already_pending"
        );

        // Ha már van pending kód
        if (LinkDatabase.hasPendingCode(p.getUniqueId())) {
            String existing = LinkDatabase.getPendingCode(p.getUniqueId());
            String msg = LangManager.get(lang, alreadyKey)
                    .replace("%code%", existing);
            p.sendMessage(msg);
            return true;
        }

        // Új kód
        String code = generateUniqueCode();
        pending.put(code, p.getUniqueId());
        LinkDatabase.storePendingCode(p.getUniqueId(), code);

        // Minecraft üzenet
        String msg = LangManager.get(lang, msgKey)
                .replace("%code%", code);
        p.sendMessage(msg);

        // Discord embed log
        String title = LangManager.get(lang, "discord.link.generated.title");
        String desc = LangManager.get(lang, "discord.link.generated.description")
                .replace("%player%", p.getName())
                .replace("%code%", code);

        DiscordLog.sendCategory(
                "discord.link.log-channel",
                title,
                desc
        );

        return true;
    }

    public static UUID consumeCode(String code) {
        UUID uuid = pending.remove(code);
        if (uuid != null) {
            LinkDatabase.consumePendingCode(code);
        }
        return uuid;
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 999999));
        } while (pending.containsKey(code));
        return code;
    }
}
