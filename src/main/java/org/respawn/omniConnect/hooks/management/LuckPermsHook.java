package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class LuckPermsHook implements Listener {

    private final String pluginKey;

    public LuckPermsHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] LuckPerms hook has been enabled!");
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onLuckPermsEvent(Event event) {
        String name = event.getClass().getName();

        try {
            String lang = lang();

            switch (name) {

                case "net.luckperms.api.event.node.NodeAddEvent": {
                    Object target = event.getClass().getMethod("getTarget").invoke(event);
                    Object node = event.getClass().getMethod("getNode").invoke(event);

                    String title = LangManager.get(lang, "hooks.management.luckperms.log.node_add.title");
                    String body =
                            LangManager.get(lang, "hooks.management.luckperms.log.node_add.target") + ": **" + target + "**\n" +
                                    LangManager.get(lang, "hooks.management.luckperms.log.node_add.node") + ": **" + node + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                case "net.luckperms.api.event.node.NodeRemoveEvent": {
                    Object target = event.getClass().getMethod("getTarget").invoke(event);
                    Object node = event.getClass().getMethod("getNode").invoke(event);

                    String title = LangManager.get(lang, "hooks.management.luckperms.log.node_remove.title");
                    String body =
                            LangManager.get(lang, "hooks.management.luckperms.log.node_remove.target") + ": **" + target + "**\n" +
                                    LangManager.get(lang, "hooks.management.luckperms.log.node_remove.node") + ": **" + node + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                case "net.luckperms.api.event.user.UserDataRecalculateEvent": {
                    Object user = event.getClass().getMethod("getUser").invoke(event);
                    String username = (String) user.getClass().getMethod("getUsername").invoke(user);

                    String title = LangManager.get(lang, "hooks.management.luckperms.log.user_recalculate.title");
                    String body =
                            LangManager.get(lang, "hooks.management.luckperms.log.user_recalculate.user") + ": **" + username + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                case "net.luckperms.api.event.group.GroupCreateEvent": {
                    Object group = event.getClass().getMethod("getGroup").invoke(event);
                    String groupName = (String) group.getClass().getMethod("getName").invoke(group);

                    String title = LangManager.get(lang, "hooks.management.luckperms.log.group_create.title");
                    String body =
                            LangManager.get(lang, "hooks.management.luckperms.log.group_create.group") + ": **" + groupName + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                case "net.luckperms.api.event.group.GroupDeleteEvent": {
                    Object group = event.getClass().getMethod("getGroup").invoke(event);
                    String groupName = (String) group.getClass().getMethod("getName").invoke(group);

                    String title = LangManager.get(lang, "hooks.management.luckperms.log.group_delete.title");
                    String body =
                            LangManager.get(lang, "hooks.management.luckperms.log.group_delete.group") + ": **" + groupName + "**";

                    DiscordLog.send(pluginKey, title, body);
                    break;
                }

                default:
                    break;
            }

        } catch (Exception ignored) {}
    }
}
