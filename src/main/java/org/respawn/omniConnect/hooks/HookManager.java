package org.respawn.omniConnect.hooks;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.anticheat.*;
import org.respawn.omniConnect.hooks.exploitfix.*;
import org.respawn.omniConnect.hooks.kingdoms.KingdomsXHook;
import org.respawn.omniConnect.hooks.moderation.*;

public class HookManager {

    public static void init() {
        FileConfiguration cfg = Main.getInstance().getConfig();

        if (!cfg.getBoolean("anticheat.enabled")) {
            Bukkit.getLogger().info("[OmniConnect] Anticheat hooks disabled.");
            return;
        }

        hook("matrix", "me.rerere.matrix.api.events.PlayerViolationEvent", MatrixHook.class);
        hook("vulcan", "me.frep.vulcan.api.event.VulcanFlagEvent", VulcanHook.class);
        hook("spartan", "me.vagdedes.spartan.api.PlayerViolationEvent", SpartanHook.class);
        hook("grim", "ac.grim.grimac.events.FlagEvent", GrimHook.class);
        hook("aac", "me.konsolas.aac.api.PlayerViolationEvent", AACHook.class);
        hook("ncp", "fr.neatmonster.nocheatplus.event.CheatEvent", NCPHook.class);
        hook("negativity", "com.elikill58.negativity.api.events.PlayerCheatEvent", NegativityHook.class);
        hook("lightanticheat", "me.lightanticheat.api.FlagEvent", LightAntiCheatHook.class);
    }
    public static void initExploitFixHooks() {
        FileConfiguration cfg = Main.getInstance().getConfig();

        if (!cfg.getBoolean("exploitfix.enabled")) {
            Bukkit.getLogger().info("[OmniConnect] ExploitFix hooks disabled.");
            return;
        }

        hook("exploitfixer", "me.gianluca_99.exploitfixer.api.ExploitDetectedEvent", ExploitFixerHook.class);
        hook("lpx", "me.lpx.antipacket.api.PacketExploitEvent", LPXAntiPacketExploitHook.class);
        hook("aef", "me.moomoo.anarchyexploitfixes.events.ExploitAlertEvent", AnarchyExploitFixesHook.class);
    }
    public static void initKingdomsHooks() {
        FileConfiguration cfg = Main.getInstance().getConfig();

        if (!cfg.getBoolean("kingdoms.enabled")) {
            Bukkit.getLogger().info("[OmniConnect] Kingdoms hook is disabled.");
            return;
        }

        hook("kingdomsx", "com.kingdomsx.api.events.KingdomEvent", KingdomsXHook.class);
    }
    public static void initModerationHooks() {
        FileConfiguration cfg = Main.getInstance().getConfig();

        if (!cfg.getBoolean("moderation.enabled")) {
            Bukkit.getLogger().info("[OmniConnect] Moderation hooks is disabled.");
            return;
        }

        // LiteBans
        hook("litebans",
                "litebans.api.EventPunishmentAdd",
                org.respawn.omniConnect.hooks.moderation.LiteBansHook.class
        );

        // AdvancedBan
        hook("advancedban",
                "me.leoko.advancedban.bukkit.event.PunishmentEvent",
                org.respawn.omniConnect.hooks.moderation.AdvancedBanHook.class
        );

        // EssentialsX Moderation (ban/mute/kick)
        hook("essentials",
                "com.earth2me.essentials.events.EssentialsBanEvent",
                org.respawn.omniConnect.hooks.moderation.EssentialsXModerationHook.class
        );

        // EssentialsX Jail/Unjail
        hook("essentialsjail",
                "com.earth2me.essentials.events.EssentialsJailEvent",
                org.respawn.omniConnect.hooks.moderation.EssentialsJailHook.class
        );

        // CMI Moderation (ban/mute/kick/jail/unjail)
        hook("cmi",
                "com.Zrips.CMI.events.CMIBanEvent",
                org.respawn.omniConnect.hooks.moderation.CMIModerationHook.class
        );

        // ChatControl (Classic)
        hook("chatcontrol",
                "com.zrips.chatcontrol.events.RuleMatchEvent",
                org.respawn.omniConnect.hooks.moderation.ChatControlHook.class
        );

        // ChatControl Red
        hook("chatcontrolred",
                "com.github.simplixsoft.chatrecontrol.api.events.RuleMatchEvent",
                org.respawn.omniConnect.hooks.moderation.ChatControlRedHook.class
        );

        // StaffPlus
        hook("staffplus",
                "net.shortninja.staffplus.core.events.PlayerReportEvent",
                org.respawn.omniConnect.hooks.moderation.StaffPlusHook.class
        );

        // StaffChat plugin
        hook("cleanstaffchat",
                "com.staffchat.api.StaffChatEvent",
                org.respawn.omniConnect.hooks.moderation.CleanStaffChatHook.class
        );
        hook("easystaff",
                "me.easystaff.api.events.StaffChatEvent",
                org.respawn.omniConnect.hooks.moderation.EasyStaffHook.class
        );

        // PlayerReport plugin
        hook("playerreport",
                "me.playerreport.api.PlayerReportEvent",
                org.respawn.omniConnect.hooks.moderation.PlayerReportHook.class
        );

        // MatrixReport
        hook("matrixreport",
                "me.rerere.matrix.api.events.PlayerReportEvent",
                org.respawn.omniConnect.hooks.moderation.MatrixReportHook.class
        );

        // VulcanReport
        hook("vulcanreport",
                "me.frep.vulcan.api.event.VulcanReportEvent",
                org.respawn.omniConnect.hooks.moderation.VulcanReportHook.class
        );

        // Liberty Bans
        hook("libertybans",
                "space.arim.libertybans.api.event.PunishmentEvent",
                org.respawn.omniConnect.hooks.moderation.LibertyBansHook.class
        );

    }
    public static void initEconomyHooks() {
        FileConfiguration cfg = Main.getInstance().getConfig();

        if (!cfg.getBoolean("economy.enabled")) {
            Bukkit.getLogger().info("[OmniConnect] Economy hooks is disabled.");
            return;
        }
        hook("playerpoints",
                "com.playerpoints.api.events.PlayerPointsChangeEvent",
                org.respawn.omniConnect.hooks.economy.PlayerPointsHook.class
        );
        hook("coinmanager",
                "me.coinmanager.api.events.CoinChangeEvent",
                org.respawn.omniConnect.hooks.economy.CoinManagerHook.class
        );
        hook("azrovaeconomy",
                "com.azrodev.economy.api.events.EconomyChangeEvent",
                org.respawn.omniConnect.hooks.economy.AzrovaEconomyHook.class
        );
        hook("essentialseconomy",
                "com.earth2me.essentials.api.events.EconomyChangeEvent",
                org.respawn.omniConnect.hooks.economy.EssentialsEconomyHook.class
        );
        hook("dzeconomy",
                "com.drexelcraft.dzeconomy.api.events.EconomyChangeEvent",
                org.respawn.omniConnect.hooks.economy.DZEconomyHook.class
        );
        hook("ezeconomy",
                "com.ezeconomy.api.events.EconomyChangeEvent",
                org.respawn.omniConnect.hooks.economy.EzEconomyHook.class
        );
        hook("xconomy",
                "com.xconomy.api.events.EconomyChangeEvent",
                org.respawn.omniConnect.hooks.economy.XConomyHook.class
        );
    }
    public static void initManagementHooks() {
        FileConfiguration cfg = Main.getInstance().getConfig();

        if (!cfg.getBoolean("management.enabled")) {
            Bukkit.getLogger().info("[OmniConnect] Management hooks is disabled.");
            return;
        }
        hook("autorestart",
                "com.autorestart.api.events.ServerRestartEvent",
                org.respawn.omniConnect.hooks.management.AutoRestartHook.class
        );
        hook("coreprotect",
                "com.coreprotect.api.events.BlockRollbackEvent",
                org.respawn.omniConnect.hooks.management.CoreProtectHook.class
        );
        hook("essentialsx",
                "com.earth2me.essentials.events.EssentialsRestartEvent",
                org.respawn.omniConnect.hooks.management.EssentialsXHook.class
        );
        hook("luckperms",
                "net.luckperms.api.event.user.UserDataRecalculateEvent",
                org.respawn.omniConnect.hooks.management.LuckPermsHook.class
        );
        hook("tab",
                "me.neznamy.tab.api.event.TabPlayerLoadEvent",
                org.respawn.omniConnect.hooks.management.TABHook.class
        );
        hook("flectonemaintenance",
                "com.flectonemaintenance.api.events.ServerMaintenanceEvent",
                org.respawn.omniConnect.hooks.management.FlectoneMaintenanceHook.class
        );
        hook("griefprevention",
                "me.ryanhamshire.GriefPrevention.event.PlayerEvent",
                org.respawn.omniConnect.hooks.management.GriefPreventionHook.class
        );
        hook("linsamaintenance",
                "com.linsama.maintenance.api.events.ServerMaintenanceEvent",
                org.respawn.omniConnect.hooks.management.LinsaMaintenanceHook.class
        );
        hook("librelogin",
                "me.librelogin.api.event.PlayerLoginEvent",
                org.respawn.omniConnect.hooks.management.LibreLoginHook.class
        );
        hook("kenzimaintenance",
                "com.kenzimaintenance.api.events.ServerMaintenanceEvent",
                org.respawn.omniConnect.hooks.management.KenziMaintenanceHook.class
        );
        hook("plugmanx",
                "com.plugmanx.api.events.PluginEnableEvent",
                org.respawn.omniConnect.hooks.management.PlugManXHook.class
        );
        hook("nametagedit",
                "me.clip.nametagedit.api.event.NametagEditEvent",
                org.respawn.omniConnect.hooks.management.NametagEditHook.class
        );
        hook("worldedit",
                "com.sk89q.worldedit.event.platform.CommandEvent",
                org.respawn.omniConnect.hooks.management.WorldEditHook.class
        );
        hook("worldguard",
                "com.sk89q.worldguard.bukkit.event.RegionEnterEvent",
                org.respawn.omniConnect.hooks.management.WorldGuardHook.class
        );
        hook("serverrestorer",
                "com.serverrestorer.api.events.ServerRestoreEvent",
                org.respawn.omniConnect.hooks.management.ServerRestorerHook.class
        );
        hook("kennytvmaintenance",
                "eu.kennytv.maintenance.api.event.MaintenanceEnableEvent",
                org.respawn.omniConnect.hooks.management.KennyTVMaintenanceHook.class
        );

    }



    private static void hook(String key, String className, Class<?> hookClass) {
        FileConfiguration cfg = Main.getInstance().getConfig();

        if (!cfg.getBoolean("anticheat." + key + ".enabled")) {
            Bukkit.getLogger().info("[OmniConnect] " + key + " hook turned off.");
            return;
        }

        try {
            Class.forName(className);
            hookClass.getDeclaredConstructor(String.class).newInstance(key);
            Bukkit.getLogger().info("[OmniConnect] " + key + " hook has been activated!");
        } catch (Exception e) {
            Bukkit.getLogger().info("[OmniConnect] " + key + " is not found.");
        }
        if (!cfg.getBoolean("exploitfix." + key + ".enabled")) {
            Bukkit.getLogger().info("[OmniConnect] " + key + " hook turned off.");
            return;
        }
        try {
            Class.forName(className);
            hookClass.getDeclaredConstructor(String.class).newInstance(key);
            Bukkit.getLogger().info("[OmniConnect] " + key + " hook activated!");
        } catch (Exception e) {
            Bukkit.getLogger().info("[OmniConnect] " + key + " is not found.");
        }
        if (!cfg.getBoolean("kingdoms." + key + ".enabled")) {
            Bukkit.getLogger().info("[OmniConnect] " + key + " hook turned off.");
            return;
        }
        try {
            Class.forName(className);
            hookClass.getDeclaredConstructor(String.class).newInstance(key);
            Bukkit.getLogger().info("[OmniConnect] " + key + " hook has been activated!");
        } catch (Exception e) {
            Bukkit.getLogger().info("[OmniConnect] " + key + " is not found.");
        }
        if (!cfg.getBoolean("moderation." + key + ".enabled")) {
            Bukkit.getLogger().info("[OmniConnect] " + key + " hook turned off.");
            return;
        }
        try {
            Class.forName(className);
            hookClass.getDeclaredConstructor(String.class).newInstance(key);
            Bukkit.getLogger().info("[OmniConnect] " + key + " hook activated!");
        } catch (Exception e) {
            Bukkit.getLogger().info("[OmniConnect] " + key + " is not found.");
        }
        if (!cfg.getBoolean("economy." + key + ".enabled")) {
            Bukkit.getLogger().info("[OmniConnect] " + key + " hook turned off.");
            return;
        }
        try {
            Class.forName(className);
            hookClass.getDeclaredConstructor(String.class).newInstance(key);
            Bukkit.getLogger().info("[OmniConnect] " + key + " hook has been activated!");
        } catch (Exception e) {
            Bukkit.getLogger().info("[OmniConnect] " + key + " is not found.");
        }
        if (!cfg.getBoolean("management." + key + ".enabled")) {
            Bukkit.getLogger().info("[OmniConnect] " + key + " hook turned off.");
            return;
        }
        try {
                Class.forName(className);
                hookClass.getDeclaredConstructor(String.class).newInstance(key);
                Bukkit.getLogger().info("[OmniConnect] " + key + " hook activated!");
            } catch (Exception e) {
                Bukkit.getLogger().info("[OmniConnect] " + key + " is not found.");
            }
    }
}
