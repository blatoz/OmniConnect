package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class ServerRestorerHook implements Listener {

    private final String pluginKey;

    public ServerRestorerHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] ServerRestorer hook aktiválva!");
    }

    @EventHandler
    public void onServerRestorerEvent(Event event) {
        String name = event.getClass().getName();

        try {
            switch (name) {

                // -------------------------
                // BackupCreateEvent
                // -------------------------
                case "me.techscode.serverrestorer.api.events.BackupCreateEvent": {
                    Object backup = event.getClass().getMethod("getBackup").invoke(event);
                    String backupName = (String) backup.getClass().getMethod("getName").invoke(backup);
                    String creator = (String) backup.getClass().getMethod("getCreator").invoke(backup);

                    DiscordLog.send(pluginKey,
                            "📦 ServerRestorer – Biztonsági Mentés Létrehozva",
                            "Biztonsági Mentés Neve: **" + backupName + "**\n"
                                    + "Készítette: **" + creator + "**"
                    );
                    break;
                }

                // -------------------------
                // BackupRestoreEvent
                // -------------------------
                case "me.techscode.serverrestorer.api.events.BackupRestoreEvent": {
                    Object backup = event.getClass().getMethod("getBackup").invoke(event);
                    String backupName = (String) backup.getClass().getMethod("getName").invoke(backup);
                    String executor = (String) event.getClass().getMethod("getExecutor").invoke(event);

                    DiscordLog.send(pluginKey,
                            "♻️ ServerRestorer – Biztonsági Mentés Visszaállítva",
                            "Biztonsági Mentés Neve: **" + backupName + "**\n"
                                    + "Visszaállította: **" + executor + "**"
                    );
                    break;
                }

                // -------------------------
                // BackupDeleteEvent
                // -------------------------
                case "me.techscode.serverrestorer.api.events.BackupDeleteEvent": {
                    Object backup = event.getClass().getMethod("getBackup").invoke(event);
                    String backupName = (String) backup.getClass().getMethod("getName").invoke(backup);
                    String executor = (String) event.getClass().getMethod("getExecutor").invoke(event);

                    DiscordLog.send(pluginKey,
                            "🗑️ ServerRestorer – Biztonsági Mentés Törölve",
                            "Biztonsági Mentés Neve: **" + backupName + "**\n"
                                    + "Törölte: **" + executor + "**"
                    );
                    break;
                }

                default:
                    break;
            }

        } catch (Exception ignored) {}
    }
}
