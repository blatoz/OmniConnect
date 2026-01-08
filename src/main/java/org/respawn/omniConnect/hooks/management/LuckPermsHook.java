package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class LuckPermsHook implements Listener {

    private final String pluginKey;

    public LuckPermsHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] LuckPerms hook aktiválva!");
    }

    @EventHandler
    public void onLuckPermsEvent(Event event) {
        String name = event.getClass().getName();

        try {
            switch (name) {

                // -------------------------
                // NodeAddEvent
                // -------------------------
                case "net.luckperms.api.event.node.NodeAddEvent": {
                    Object target = event.getClass().getMethod("getTarget").invoke(event);
                    Object node = event.getClass().getMethod("getNode").invoke(event);

                    String targetName = target.toString();
                    String nodeStr = node.toString();

                    DiscordLog.send(pluginKey,
                            "🔑 LuckPerms – Jog hozzáadva",
                            "Cél: **" + targetName + "**\n"
                                    + "Jog: **" + nodeStr + "**"
                    );
                    break;
                }

                // -------------------------
                // NodeRemoveEvent
                // -------------------------
                case "net.luckperms.api.event.node.NodeRemoveEvent": {
                    Object target = event.getClass().getMethod("getTarget").invoke(event);
                    Object node = event.getClass().getMethod("getNode").invoke(event);

                    String targetName = target.toString();
                    String nodeStr = node.toString();

                    DiscordLog.send(pluginKey,
                            "🗑️ LuckPerms – Jog eltávolítva",
                            "Cél: **" + targetName + "**\n"
                                    + "Jog: **" + nodeStr + "**"
                    );
                    break;
                }

                // -------------------------
                // UserDataRecalculateEvent
                // -------------------------
                case "net.luckperms.api.event.user.UserDataRecalculateEvent": {
                    Object user = event.getClass().getMethod("getUser").invoke(event);
                    String username = (String) user.getClass().getMethod("getUsername").invoke(user);

                    DiscordLog.send(pluginKey,
                            "♻️ LuckPerms – User adat újraszámolva",
                            "Felhasználó: **" + username + "**"
                    );
                    break;
                }

                // -------------------------
                // GroupCreateEvent
                // -------------------------
                case "net.luckperms.api.event.group.GroupCreateEvent": {
                    Object group = event.getClass().getMethod("getGroup").invoke(event);
                    String groupName = (String) group.getClass().getMethod("getName").invoke(group);

                    DiscordLog.send(pluginKey,
                            "📁 LuckPerms – Csoport létrehozva",
                            "Csoport: **" + groupName + "**"
                    );
                    break;
                }

                // -------------------------
                // GroupDeleteEvent
                // -------------------------
                case "net.luckperms.api.event.group.GroupDeleteEvent": {
                    Object group = event.getClass().getMethod("getGroup").invoke(event);
                    String groupName = (String) group.getClass().getMethod("getName").invoke(group);

                    DiscordLog.send(pluginKey,
                            "🗑️ LuckPerms – Csoport törölve",
                            "Csoport: **" + groupName + "**"
                    );
                    break;
                }

                default:
                    break;
            }

        } catch (Exception ignored) {}
    }
}
