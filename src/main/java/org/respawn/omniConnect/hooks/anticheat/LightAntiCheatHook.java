package org.respawn.omniConnect.hooks.anticheat;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class LightAntiCheatHook implements Listener {

    private final String pluginKey;

    public LightAntiCheatHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onFlag(Event event) {
        if (!event.getClass().getName().equals("me.lightanticheat.api.FlagEvent"))
            return;

        try {
            String lang = lang();

            Object player = event.getClass().getMethod("getPlayer").invoke(event);
            Object check = event.getClass().getMethod("getCheck").invoke(event);
            int vl = (int) event.getClass().getMethod("getVl").invoke(event);

            String playerName = (String) player.getClass().getMethod("getName").invoke(player);
            String checkName = check.toString();

            String title = LangManager.get(lang, "hooks.anticheat.lightanticheat.log.violation.title");

            String body =
                    LangManager.get(lang, "hooks.anticheat.lightanticheat.log.violation.player") + ": **" + playerName + "**\n" +
                            LangManager.get(lang, "hooks.anticheat.lightanticheat.log.violation.hack_type") + ": **" + checkName + "**\n" +
                            LangManager.get(lang, "hooks.anticheat.lightanticheat.log.violation.vl") + ": **" + vl + "**";

            DiscordLog.send(pluginKey, title, body);

        } catch (Exception ignored) {}
    }
}
