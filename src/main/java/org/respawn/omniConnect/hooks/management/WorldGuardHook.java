package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class WorldGuardHook implements Listener {

    private final String pluginKey;

    public WorldGuardHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] WorldGuard hook has been enabled!");
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onWorldGuardEvent(Event event) {
        String name = event.getClass().getName();

        try {
            String lang = lang();

            // RegionCreateEvent
            if (name.equals("com.sk89q.worldguard.bukkit.event.region.RegionCreateEvent")) {

                Object region = event.getClass().getMethod("getRegion").invoke(event);
                String id = (String) region.getClass().getMethod("getId").invoke(region);

                Object actor = event.getClass().getMethod("getActor").invoke(event);
                String executor = actor != null ? actor.toString() : "Ismeretlen";

                String title = LangManager.get(lang, "hooks.management.worldguard.log.create.title");
                String body =
                        LangManager.get(lang, "hooks.management.worldguard.log.create.region") + ": **" + id + "**\n" +
                                LangManager.get(lang, "hooks.management.worldguard.log.create.executor") + ": **" + executor + "**";

                DiscordLog.send(pluginKey, title, body);
            }

            // RegionDeleteEvent
            if (name.equals("com.sk89q.worldguard.bukkit.event.region.RegionDeleteEvent")) {

                String id = (String) event.getClass().getMethod("getRegionId").invoke(event);

                Object actor = event.getClass().getMethod("getActor").invoke(event);
                String executor = actor != null ? actor.toString() : "Ismeretlen";

                String title = LangManager.get(lang, "hooks.management.worldguard.log.delete.title");
                String body =
                        LangManager.get(lang, "hooks.management.worldguard.log.delete.region") + ": **" + id + "**\n" +
                                LangManager.get(lang, "hooks.management.worldguard.log.delete.executor") + ": **" + executor + "**";

                DiscordLog.send(pluginKey, title, body);
            }

            // RegionUpdateEvent
            if (name.equals("com.sk89q.worldguard.bukkit.event.region.RegionUpdateEvent")) {

                Object region = event.getClass().getMethod("getRegion").invoke(event);
                String id = (String) region.getClass().getMethod("getId").invoke(region);

                Object actor = event.getClass().getMethod("getActor").invoke(event);
                String executor = actor != null ? actor.toString() : "Ismeretlen";

                String title = LangManager.get(lang, "hooks.management.worldguard.log.update.title");
                String body =
                        LangManager.get(lang, "hooks.management.worldguard.log.update.region") + ": **" + id + "**\n" +
                                LangManager.get(lang, "hooks.management.worldguard.log.update.executor") + ": **" + executor + "**";

                DiscordLog.send(pluginKey, title, body);
            }

            // FlagValueChangeEvent
            if (name.equals("com.sk89q.worldguard.bukkit.event.flag.FlagValueChangeEvent")) {

                Object flag = event.getClass().getMethod("getFlag").invoke(event);
                Object oldVal = event.getClass().getMethod("getOldValue").invoke(event);
                Object newVal = event.getClass().getMethod("getNewValue").invoke(event);

                Object region = event.getClass().getMethod("getRegion").invoke(event);
                String id = (String) region.getClass().getMethod("getId").invoke(region);

                Object actor = event.getClass().getMethod("getActor").invoke(event);
                String executor = actor != null ? actor.toString() : "Ismeretlen";

                String title = LangManager.get(lang, "hooks.management.worldguard.log.flag_change.title");
                String body =
                        LangManager.get(lang, "hooks.management.worldguard.log.flag_change.region") + ": **" + id + "**\n" +
                                LangManager.get(lang, "hooks.management.worldguard.log.flag_change.flag") + ": **" + flag + "**\n" +
                                LangManager.get(lang, "hooks.management.worldguard.log.flag_change.old_value") + ": **" + oldVal + "**\n" +
                                LangManager.get(lang, "hooks.management.worldguard.log.flag_change.new_value") + ": **" + newVal + "**\n" +
                                LangManager.get(lang, "hooks.management.worldguard.log.flag_change.executor") + ": **" + executor + "**";

                DiscordLog.send(pluginKey, title, body);
            }

        } catch (Exception ignored) {}
    }
}
