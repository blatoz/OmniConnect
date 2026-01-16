package org.respawn.omniConnect.hooks.anticheat;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class NCPHook implements Listener {

    private final String pluginKey;

    public NCPHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onCheat(Event event) {
        if (!event.getClass().getName().equals("fr.neatmonster.nocheatplus.event.CheatEvent"))
            return;

        try {
            String lang = lang();

            Object player = event.getClass().getMethod("getPlayer").invoke(event);
            Object type = event.getClass().getMethod("getType").invoke(event);
            int vl = (int) event.getClass().getMethod("getLevel").invoke(event);

            String playerName = (String) player.getClass().getMethod("getName").invoke(player);

            String title = LangManager.get(lang, "hooks.anticheat.ncp.log.violation.title");

            String body =
                    LangManager.get(lang, "hooks.anticheat.ncp.log.violation.player") + ": **" + playerName + "**\n" +
                            LangManager.get(lang, "hooks.anticheat.ncp.log.violation.hack_type") + ": **" + type.toString() + "**\n" +
                            LangManager.get(lang, "hooks.anticheat.ncp.log.violation.vl") + ": **" + vl + "**";

            DiscordLog.send(pluginKey, title, body);

        } catch (Exception ignored) {}
    }
}
