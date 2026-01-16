package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class WorldEditHook implements Listener {

    private final String pluginKey;

    public WorldEditHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] WorldEdit hook has been enabled!");
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onWorldEditCommand(PlayerCommandPreprocessEvent event) {
        String msg = event.getMessage();
        if (!isWorldEditCommand(msg)) return;

        String lang = lang();
        String playerName = event.getPlayer().getName();

        String title = LangManager.get(lang, "hooks.management.worldedit.log.command.title");
        String body =
                LangManager.get(lang, "hooks.management.worldedit.log.command.player") + ": **" + playerName + "**\n" +
                        LangManager.get(lang, "hooks.management.worldedit.log.command.command") + ": `" + msg + "`";

        DiscordLog.send(pluginKey, title, body);
    }

    private boolean isWorldEditCommand(String raw) {
        if (raw == null) return false;

        String cmd = raw.trim().toLowerCase();
        return cmd.startsWith("//")
                || cmd.startsWith("/worldedit")
                || cmd.startsWith("/we ")
                || cmd.equals("/we");
    }

    @EventHandler
    public void onWorldEditEvent(Event event) {
        String name = event.getClass().getName();

        try {
            if (name.equals("com.sk89q.worldedit.event.extent.EditSessionEvent")) {

                String lang = lang();

                Object actor = event.getClass().getMethod("getActor").invoke(event);
                String executor = actor != null ? actor.toString() : "Ismeretlen";

                Object world = event.getClass().getMethod("getWorld").invoke(event);
                String worldName = world != null ? world.toString() : "Ismeretlen";

                String title = LangManager.get(lang, "hooks.management.worldedit.log.editsession.title");
                String body =
                        LangManager.get(lang, "hooks.management.worldedit.log.editsession.executor") + ": **" + executor + "**\n" +
                                LangManager.get(lang, "hooks.management.worldedit.log.editsession.world") + ": **" + worldName + "**";

                DiscordLog.send(pluginKey, title, body);
            }

        } catch (Exception ignored) {}
    }
}
