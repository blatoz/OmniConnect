package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class TABHook implements Listener {

    private final String pluginKey;

    public TABHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] TAB hook has been enabled!");
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onTabEvent(Event event) {
        String name = event.getClass().getName();

        try {
            if (name.equals("me.neznamy.tab.api.event.player.PlayerPrefixSuffixChangeEvent")) {

                String lang = lang();

                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                String playerName = player != null ? player.toString() : "Ismeretlen";

                String oldPrefix = (String) event.getClass().getMethod("getOldPrefix").invoke(event);
                String newPrefix = (String) event.getClass().getMethod("getNewPrefix").invoke(event);

                String oldSuffix = (String) event.getClass().getMethod("getOldSuffix").invoke(event);
                String newSuffix = (String) event.getClass().getMethod("getNewSuffix").invoke(event);

                String title = LangManager.get(lang, "hooks.management.tab.log.tag_change.title");

                String body =
                        LangManager.get(lang, "hooks.management.tab.log.tag_change.player") + ": **" + playerName + "**\n" +
                                LangManager.get(lang, "hooks.management.tab.log.tag_change.old_prefix") + ": `" + oldPrefix + "`\n" +
                                LangManager.get(lang, "hooks.management.tab.log.tag_change.new_prefix") + ": `" + newPrefix + "`\n" +
                                LangManager.get(lang, "hooks.management.tab.log.tag_change.old_suffix") + ": `" + oldSuffix + "`\n" +
                                LangManager.get(lang, "hooks.management.tab.log.tag_change.new_suffix") + ": `" + newSuffix + "`";

                DiscordLog.send(pluginKey, title, body);
            }

        } catch (Exception ignored) {}
    }
}
