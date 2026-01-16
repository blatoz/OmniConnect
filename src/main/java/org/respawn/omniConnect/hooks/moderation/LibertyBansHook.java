package org.respawn.omniConnect.hooks.moderation;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class LibertyBansHook implements Listener {

    private final String pluginKey;

    public LibertyBansHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onLibertyBans(Event event) {
        if (!event.getClass().getName().equals("space.arim.libertybans.api.event.PunishmentEvent"))
            return;

        try {
            String lang = lang();

            Object punishment = event.getClass().getMethod("getPunishment").invoke(event);

            String type = punishment.getClass().getMethod("getType").invoke(punishment).toString();
            String target = punishment.getClass().getMethod("getTargetName").invoke(punishment).toString();
            String operator = punishment.getClass().getMethod("getOperatorName").invoke(punishment).toString();
            String reason = punishment.getClass().getMethod("getReason").invoke(punishment).toString();

            String title = LangManager.get(lang, "hooks.moderation.libertybans.log.punishment.title");

            String body =
                    LangManager.get(lang, "hooks.moderation.libertybans.log.punishment.type") + ": **" + type + "**\n" +
                            LangManager.get(lang, "hooks.moderation.libertybans.log.punishment.player") + ": **" + target + "**\n" +
                            LangManager.get(lang, "hooks.moderation.libertybans.log.punishment.staff") + ": **" + operator + "**\n" +
                            LangManager.get(lang, "hooks.moderation.libertybans.log.punishment.reason") + ": **" + reason + "**";

            DiscordLog.send(pluginKey, title, body);

        } catch (Exception ignored) {}
    }
}
