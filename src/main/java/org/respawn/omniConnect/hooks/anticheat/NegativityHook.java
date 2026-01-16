package org.respawn.omniConnect.hooks.anticheat;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class NegativityHook implements Listener {

    private final String pluginKey;

    public NegativityHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onCheat(Event event) {
        if (!event.getClass().getName().equals("com.elikill58.negativity.api.events.PlayerCheatEvent"))
            return;

        try {
            String lang = lang();

            Object player = event.getClass().getMethod("getPlayer").invoke(event);
            Object cheat = event.getClass().getMethod("getCheat").invoke(event);
            int reliability = (int) event.getClass().getMethod("getReliability").invoke(event);

            String playerName = (String) player.getClass().getMethod("getName").invoke(player);
            String cheatName = (String) cheat.getClass().getMethod("getName").invoke(cheat);

            String title = LangManager.get(lang, "hooks.anticheat.negativity.log.violation.title");

            String body =
                    LangManager.get(lang, "hooks.anticheat.negativity.log.violation.player") + ": **" + playerName + "**\n" +
                            LangManager.get(lang, "hooks.anticheat.negativity.log.violation.hack_type") + ": **" + cheatName + "**\n" +
                            LangManager.get(lang, "hooks.anticheat.negativity.log.violation.reliability") + ": **" + reliability + "**";

            DiscordLog.send(pluginKey, title, body);

        } catch (Exception ignored) {}
    }
}
