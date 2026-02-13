package org.respawn.omniConnect.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Rang/csoport kezelő - LuckPerms és Scoreboard támogatással.
 */
public class RankUtil {

    /**
     * Játékos rangjának lekérése LuckPerms vagy Scoreboard alapján.
     *
     * @param player A játékos
     * @return A rang neve vagy üres string, ha nincs
     */
    public static String getRank(Player player) {
        // LuckPerms support (through reflection, so that the entire API is not required)
        if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
            try {
                return getLuckPermsRank(player);
            } catch (Exception e) {
                Bukkit.getLogger().warning("LuckPerms getRank hiba: " + e.getMessage());
            }
        }

        // Scoreboard prefix fallback
        try {
            var team = player.getScoreboard().getEntryTeam(player.getName());
            if (team != null) {
                String prefix = team.getPrefix();
                if (!prefix.isEmpty()) {
                    return prefix;
                }
            }
        } catch (Exception e) {
            Bukkit.getLogger().warning("Scoreboard prefix hiba: " + e.getMessage());
        }

        return "";
    }

    private static String getLuckPermsRank(Player player) throws Exception {
        // LuckPerms API Frist group fetch with reflection
        Class<?> providerClass = Class.forName("net.luckperms.api.LuckPermsProvider");
        Object lpInstance = providerClass.getMethod("get").invoke(null);

        // UserManager Frist group fetch
        Object userManager = lpInstance.getClass().getMethod("getUserManager").invoke(lpInstance);
        Object user = userManager.getClass().getMethod("getUser", java.util.UUID.class)
                .invoke(userManager, player.getUniqueId());

        if (user != null) {
            // Frist group fetch
            Object primaryGroup = user.getClass().getMethod("getPrimaryGroup").invoke(user);
            if (primaryGroup != null) {
                String groupName = primaryGroup.toString();
                if (!groupName.isEmpty()) {
                    return groupName;
                }
            }
        }

        return "";
    }
}
