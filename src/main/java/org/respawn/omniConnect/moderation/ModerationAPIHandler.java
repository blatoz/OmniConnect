package org.respawn.omniConnect.moderation;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.respawn.omniConnect.config.OmniConfig;
import org.respawn.omniConnect.hooks.moderation.AdvancedBanHook;
import org.respawn.omniConnect.hooks.moderation.LiteBansHook;

public class ModerationAPIHandler {

    public static void warn(OfflinePlayer target, String reason, String moderator) {
        if (OmniConfig.isLiteBansEnabled()) {
            LiteBansHook.warn(target, reason, moderator);
        } else if (OmniConfig.isAdvancedBanEnabled()) {
            AdvancedBanHook.warn(target, reason, moderator);
        } else if (OmniConfig.isEssentialsEnabled()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    "warn " + target.getName() + " " + reason);
        }
    }

    public static void mute(OfflinePlayer target, String reason, String moderator) {
        if (OmniConfig.isLiteBansEnabled()) {
            LiteBansHook.mute(target, reason, moderator);
        } else if (OmniConfig.isAdvancedBanEnabled()) {
            AdvancedBanHook.mute(target, reason, moderator);
        } else if (OmniConfig.isEssentialsEnabled()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    "mute " + target.getName() + " " + reason);
        }
    }

    public static void tempMute(OfflinePlayer target, long minutes, String reason, String moderator) {
        if (OmniConfig.isLiteBansEnabled()) {
            LiteBansHook.tempMute(target, minutes, reason, moderator);
        } else if (OmniConfig.isAdvancedBanEnabled()) {
            AdvancedBanHook.tempMute(target, minutes, reason, moderator);
        } else if (OmniConfig.isEssentialsEnabled()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    "tempmute " + target.getName() + " " + minutes + "m " + reason);
        }
    }

    public static void kick(OfflinePlayer target, String reason, String moderator) {
        if (OmniConfig.isLiteBansEnabled()) {
            LiteBansHook.kick(target, reason, moderator);
        } else if (OmniConfig.isAdvancedBanEnabled()) {
            AdvancedBanHook.kick(target, reason, moderator);
        } else if (OmniConfig.isEssentialsEnabled()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    "kick " + target.getName() + " " + reason);
        }
    }

    public static void ban(OfflinePlayer target, String reason, String moderator) {
        if (OmniConfig.isLiteBansEnabled()) {
            LiteBansHook.ban(target, reason, moderator);
        } else if (OmniConfig.isAdvancedBanEnabled()) {
            AdvancedBanHook.ban(target, reason, moderator);
        } else if (OmniConfig.isEssentialsEnabled()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    "ban " + target.getName() + " " + reason);
        }
    }
}
