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
            Bukkit.getLogger().info("[OmniConnect] Anticheat hookok letiltva.");
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
            Bukkit.getLogger().info("[OmniConnect] ExploitFix hookok letiltva.");
            return;
        }

        hook("exploitfixer", "me.gianluca_99.exploitfixer.api.ExploitDetectedEvent", ExploitFixerHook.class);
        hook("lpx", "me.lpx.antipacket.api.PacketExploitEvent", LPXAntiPacketExploitHook.class);
        hook("aef", "me.moomoo.anarchyexploitfixes.events.ExploitAlertEvent", AnarchyExploitFixesHook.class);
    }
    public static void initKingdomsHooks() {
        FileConfiguration cfg = Main.getInstance().getConfig();

        if (!cfg.getBoolean("kingdoms.enabled")) {
            Bukkit.getLogger().info("[OmniConnect] Kingdoms hookok letiltva.");
            return;
        }

        hook("kingdomsx", "com.kingdomsx.api.events.KingdomEvent", KingdomsXHook.class);
    }
    public static void initModerationHooks() {
        FileConfiguration cfg = Main.getInstance().getConfig();

        if (!cfg.getBoolean("moderation.enabled")) {
            Bukkit.getLogger().info("[OmniConnect] Moderációs hookok letiltva.");
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
        hook("staffchat",
                "com.staffchat.api.StaffChatEvent",
                org.respawn.omniConnect.hooks.moderation.StaffChatHook.class
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


    private static void hook(String key, String className, Class<?> hookClass) {
        FileConfiguration cfg = Main.getInstance().getConfig();

        if (!cfg.getBoolean("anticheat." + key + ".enabled")) {
            Bukkit.getLogger().info("[OmniConnect] " + key + " hook kikapcsolva.");
            return;
        }

        try {
            Class.forName(className);
            hookClass.getDeclaredConstructor(String.class).newInstance(key);
            Bukkit.getLogger().info("[OmniConnect] " + key + " hook aktiválva!");
        } catch (Exception e) {
            Bukkit.getLogger().info("[OmniConnect] " + key + " nem található.");
        }
        if (!cfg.getBoolean("exploitfix." + key + ".enabled")) {
            Bukkit.getLogger().info("[OmniConnect] " + key + " hook kikapcsolva.");
            return;
        }
        try {
            Class.forName(className);
            hookClass.getDeclaredConstructor(String.class).newInstance(key);
            Bukkit.getLogger().info("[OmniConnect] " + key + " hook aktiválva!");
        } catch (Exception e) {
            Bukkit.getLogger().info("[OmniConnect] " + key + " nem található.");
        }
        if (!cfg.getBoolean("kingdoms." + key + ".enabled")) {
            Bukkit.getLogger().info("[OmniConnect] " + key + " hook kikapcsolva.");
            return;
        }
        try {
            Class.forName(className);
            hookClass.getDeclaredConstructor(String.class).newInstance(key);
            Bukkit.getLogger().info("[OmniConnect] " + key + " hook aktiválva!");
        } catch (Exception e) {
            Bukkit.getLogger().info("[OmniConnect] " + key + " nem található.");
        }
        if (!cfg.getBoolean("moderation." + key + ".enabled")) {
            Bukkit.getLogger().info("[OmniConnect] " + key + " hook kikapcsolva.");
            return;
        }
        try {
            Class.forName(className);
            hookClass.getDeclaredConstructor(String.class).newInstance(key);
            Bukkit.getLogger().info("[OmniConnect] " + key + " hook aktiválva!");
        } catch (Exception e) {
            Bukkit.getLogger().info("[OmniConnect] " + key + " nem található.");
        }
    }
}
