package org.respawn.omniConnect.hooks.management;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class GriefPreventionHook implements Listener {

    private final String pluginKey;

    public GriefPreventionHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getLogger().info("[OmniConnect] GriefPrevention hook has been enabled!");
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    @EventHandler
    public void onGriefPreventionEvent(Event event) {
        String name = event.getClass().getName();

        try {
            String lang = lang();

            // ClaimCreateEvent
            if (name.equals("me.ryanhamshire.GriefPrevention.events.ClaimCreatedEvent")) {

                Object claim = event.getClass().getMethod("getClaim").invoke(event);
                Object player = event.getClass().getMethod("getCreator").invoke(event);

                String title = LangManager.get(lang, "hooks.management.griefprevention.log.claim_create.title");
                String body =
                        LangManager.get(lang, "hooks.management.griefprevention.log.claim_create.player") + ": **" + player + "**\n" +
                                LangManager.get(lang, "hooks.management.griefprevention.log.claim_create.claim") + ": **" + claim + "**";

                DiscordLog.send(pluginKey, title, body);
            }

            // ClaimDeleteEvent
            if (name.equals("me.ryanhamshire.GriefPrevention.events.ClaimDeletedEvent")) {

                Object claim = event.getClass().getMethod("getClaim").invoke(event);
                Object player = event.getClass().getMethod("getClaimant").invoke(event);

                String title = LangManager.get(lang, "hooks.management.griefprevention.log.claim_delete.title");
                String body =
                        LangManager.get(lang, "hooks.management.griefprevention.log.claim_delete.player") + ": **" + player + "**\n" +
                                LangManager.get(lang, "hooks.management.griefprevention.log.claim_delete.claim") + ": **" + claim + "**";

                DiscordLog.send(pluginKey, title, body);
            }

            // ClaimResizeEvent
            if (name.equals("me.ryanhamshire.GriefPrevention.events.ClaimModifiedEvent")) {

                Object claim = event.getClass().getMethod("getClaim").invoke(event);
                Object player = event.getClass().getMethod("getModifier").invoke(event);

                String title = LangManager.get(lang, "hooks.management.griefprevention.log.claim_resize.title");
                String body =
                        LangManager.get(lang, "hooks.management.griefprevention.log.claim_resize.player") + ": **" + player + "**\n" +
                                LangManager.get(lang, "hooks.management.griefprevention.log.claim_resize.claim") + ": **" + claim + "**";

                DiscordLog.send(pluginKey, title, body);
            }

            // TrustChangedEvent
            if (name.equals("me.ryanhamshire.GriefPrevention.events.TrustChangedEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                Object target = event.getClass().getMethod("getTarget").invoke(event);
                Object trust = event.getClass().getMethod("getTrustType").invoke(event);

                String title = LangManager.get(lang, "hooks.management.griefprevention.log.trust_change.title");
                String body =
                        LangManager.get(lang, "hooks.management.griefprevention.log.trust_change.player") + ": **" + player + "**\n" +
                                LangManager.get(lang, "hooks.management.griefprevention.log.trust_change.target") + ": **" + target + "**\n" +
                                LangManager.get(lang, "hooks.management.griefprevention.log.trust_change.trust") + ": **" + trust + "**";

                DiscordLog.send(pluginKey, title, body);
            }

            // WarnPlayerEvent
            if (name.equals("me.ryanhamshire.GriefPrevention.events.WarnPlayerEvent")) {

                Object player = event.getClass().getMethod("getPlayer").invoke(event);
                Object reason = event.getClass().getMethod("getReason").invoke(event);

                String title = LangManager.get(lang, "hooks.management.griefprevention.log.warn.title");
                String body =
                        LangManager.get(lang, "hooks.management.griefprevention.log.warn.player") + ": **" + player + "**\n" +
                                LangManager.get(lang, "hooks.management.griefprevention.log.warn.reason") + ": **" + reason + "**";

                DiscordLog.send(pluginKey, title, body);
            }

        } catch (Exception ignored) {}
    }
}
