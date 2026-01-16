package org.respawn.omniConnect.hooks.anticheat;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class VulcanHook implements Listener {

    private final String pluginKey;

    public VulcanHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onFlag(Event event) {
        if (!event.getClass().getName().equals("me.frep.vulcan.api.event.VulcanFlagEvent"))
            return;

        try {
            String lang = lang();

            Object player = event.getClass().getMethod("getPlayer").invoke(event);
            Object check = event.getClass().getMethod("getCheck").invoke(event);

            String playerName = (String) player.getClass().getMethod("getName").invoke(player);
            String checkName = (String) check.getClass().getMethod("getName").invoke(check);
            int vl = (int) check.getClass().getMethod("getVl").invoke(check);

            String title = LangManager.get(lang, "hooks.anticheat.vulcan.log.violation.title");

            String body =
                    LangManager.get(lang, "hooks.anticheat.vulcan.log.violation.player") + ": **" + playerName + "**\n" +
                            LangManager.get(lang, "hooks.anticheat.vulcan.log.violation.hack_type") + ": **" + checkName + "**\n" +
                            LangManager.get(lang, "hooks.anticheat.vulcan.log.violation.vl") + ": **" + vl + "**";

            DiscordLog.send(pluginKey, title, body);

        } catch (Exception ignored) {}
    }
}
