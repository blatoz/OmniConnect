package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class ServerRestorerHook implements Listener {

    private final String pluginKey;

    public ServerRestorerHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] ServerRestorer hook has been enabled!");
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onServerRestorerEvent(Event event) {
        String name = event.getClass().getName();

        try {
            String lang = lang();

            switch (name) {

                case "me.techscode.serverrestorer.api.events.BackupCreateEvent": {
                    Object backup = event.getClass().getMethod("getBackup").invoke(event);
                    String backupName = (String) backup.getClass().getMethod("getName").invoke(backup);
                    String creator = (String) backup.getClass().getMethod("getCreator").invoke(backup);

                    String title = LangManager.get(lang, "hooks.management.serverrestorer.log.create.title");
                    String body =
                            LangManager.get(lang, "hooks.management.serverrestorer.log.create.backup") + ": **" + backupName + "**\n" +
                                    LangManager.get(lang, "hooks.management.serverrestorer.log.create.creator") + ": **" + creator + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                case "me.techscode.serverrestorer.api.events.BackupRestoreEvent": {
                    Object backup = event.getClass().getMethod("getBackup").invoke(event);
                    String backupName = (String) backup.getClass().getMethod("getName").invoke(backup);
                    String executor = (String) event.getClass().getMethod("getExecutor").invoke(event);

                    String title = LangManager.get(lang, "hooks.management.serverrestorer.log.restore.title");
                    String body =
                            LangManager.get(lang, "hooks.management.serverrestorer.log.restore.backup") + ": **" + backupName + "**\n" +
                                    LangManager.get(lang, "hooks.management.serverrestorer.log.restore.executor") + ": **" + executor + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                case "me.techscode.serverrestorer.api.events.BackupDeleteEvent": {
                    Object backup = event.getClass().getMethod("getBackup").invoke(event);
                    String backupName = (String) backup.getClass().getMethod("getName").invoke(backup);
                    String executor = (String) event.getClass().getMethod("getExecutor").invoke(event);

                    String title = LangManager.get(lang, "hooks.management.serverrestorer.log.delete.title");
                    String body =
                            LangManager.get(lang, "hooks.management.serverrestorer.log.delete.backup") + ": **" + backupName + "**\n" +
                                    LangManager.get(lang, "hooks.management.serverrestorer.log.delete.executor") + ": **" + executor + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                default:
                    break;
            }

        } catch (Exception ignored) {}
    }
}
