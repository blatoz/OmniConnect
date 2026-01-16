package org.respawn.omniConnect.hooks.moderation;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class LiteBansHook implements Listener {

    private final String pluginKey;

    public LiteBansHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onPunishment(Event event) {
        if (!event.getClass().getName().equals("litebans.api.EventPunishmentAdd"))
            return;

        try {
            String lang = lang();

            Object punishment = event.getClass().getMethod("getPunishment").invoke(event);

            String type = (String) punishment.getClass().getMethod("getType").invoke(punishment);
            String target = (String) punishment.getClass().getMethod("getName").invoke(punishment);
            String executor = (String) punishment.getClass().getMethod("getExecutorName").invoke(punishment);
            String reason = (String) punishment.getClass().getMethod("getReason").invoke(punishment);

            String title = LangManager.get(lang, "hooks.moderation.litebans.log.punishment.title");

            String body =
                    LangManager.get(lang, "hooks.moderation.litebans.log.punishment.type") + ": **" + type + "**\n" +
                            LangManager.get(lang, "hooks.moderation.litebans.log.punishment.player") + ": **" + target + "**\n" +
                            LangManager.get(lang, "hooks.moderation.litebans.log.punishment.staff") + ": **" + executor + "**\n" +
                            LangManager.get(lang, "hooks.moderation.litebans.log.punishment.reason") + ": **" + reason + "**";

            DiscordLog.send(pluginKey, title, body);

        } catch (Exception ignored) {}
    }
}
