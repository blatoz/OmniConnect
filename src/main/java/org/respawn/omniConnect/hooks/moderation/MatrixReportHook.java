package org.respawn.omniConnect.hooks.moderation;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class MatrixReportHook implements Listener {

    private final String pluginKey;

    public MatrixReportHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onMatrixReport(Event event) {
        if (!event.getClass().getName().equals("me.rerere.matrix.api.events.PlayerReportEvent"))
            return;

        try {
            String lang = lang();

            Object reporter = event.getClass().getMethod("getReporter").invoke(event);
            Object target = event.getClass().getMethod("getTarget").invoke(event);
            Object reason = event.getClass().getMethod("getReason").invoke(event);

            String reporterName = (String) reporter.getClass().getMethod("getName").invoke(reporter);
            String targetName = (String) target.getClass().getMethod("getName").invoke(target);

            String title = LangManager.get(lang, "hooks.moderation.matrixreport.log.report.title");

            String body =
                    LangManager.get(lang, "hooks.moderation.matrixreport.log.report.reporter") + ": **" + reporterName + "**\n" +
                            LangManager.get(lang, "hooks.moderation.matrixreport.log.report.target") + ": **" + targetName + "**\n" +
                            LangManager.get(lang, "hooks.moderation.matrixreport.log.report.reason") + ": **" + reason.toString() + "**";

            DiscordLog.send(pluginKey, title, body);

        } catch (Exception ignored) {}
    }
}
