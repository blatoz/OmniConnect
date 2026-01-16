package org.respawn.omniConnect.hooks.kingdoms;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;
import org.respawn.omniConnect.lang.LangManager;

public class KingdomsXHook implements Listener {

    private final String pluginKey;

    public KingdomsXHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    private String lang() {
        return LangManager.getDefaultLanguage();
    }

    private void send(String titleKey, String body) {
        String lang = lang();
        String title = LangManager.get(lang, titleKey);
        DiscordLog.send(pluginKey, title, body);
    }

    @EventHandler
    public void onKingdomEvent(Event event) {
        String className = event.getClass().getName();
        String lang = lang();

        try {
            switch (className) {

                case "org.kingdoms.events.general.KingdomCreateEvent": {
                    Object creator = event.getClass().getMethod("getCreator").invoke(event);
                    Object kingdom = event.getClass().getMethod("getKingdom").invoke(event);

                    String player = (String) creator.getClass().getMethod("getName").invoke(creator);
                    String kingdomName = (String) kingdom.getClass().getMethod("getName").invoke(kingdom);

                    String body =
                            LangManager.get(lang, "kingdoms.log.create.player") + ": **" + player + "**\n" +
                                    LangManager.get(lang, "kingdoms.log.create.kingdom") + ": **" + kingdomName + "**";

                    send("kingdoms.log.create.title", body);
                    break;
                }

                case "org.kingdoms.events.general.KingdomDisbandEvent": {
                    Object executor = event.getClass().getMethod("getExecutor").invoke(event);
                    Object kingdom = event.getClass().getMethod("getKingdom").invoke(event);

                    String player = (String) executor.getClass().getMethod("getName").invoke(executor);
                    String kingdomName = (String) kingdom.getClass().getMethod("getName").invoke(kingdom);

                    String body =
                            LangManager.get(lang, "kingdoms.log.disband.player") + ": **" + player + "**\n" +
                                    LangManager.get(lang, "kingdoms.log.disband.kingdom") + ": **" + kingdomName + "**";

                    send("kingdoms.log.disband.title", body);
                    break;
                }

                case "org.kingdoms.events.members.KingdomJoinEvent": {
                    Object playerObj = event.getClass().getMethod("getPlayer").invoke(event);
                    Object kingdom = event.getClass().getMethod("getKingdom").invoke(event);

                    String player = (String) playerObj.getClass().getMethod("getName").invoke(playerObj);
                    String kingdomName = (String) kingdom.getClass().getMethod("getName").invoke(kingdom);

                    String body =
                            LangManager.get(lang, "kingdoms.log.join.player") + ": **" + player + "**\n" +
                                    LangManager.get(lang, "kingdoms.log.join.kingdom") + ": **" + kingdomName + "**";

                    send("kingdoms.log.join.title", body);
                    break;
                }

                case "org.kingdoms.events.members.KingdomLeaveEvent": {
                    Object playerObj = event.getClass().getMethod("getPlayer").invoke(event);
                    Object kingdom = event.getClass().getMethod("getKingdom").invoke(event);

                    String player = (String) playerObj.getClass().getMethod("getName").invoke(playerObj);
                    String kingdomName = (String) kingdom.getClass().getMethod("getName").invoke(kingdom);

                    String body =
                            LangManager.get(lang, "kingdoms.log.leave.player") + ": **" + player + "**\n" +
                                    LangManager.get(lang, "kingdoms.log.leave.kingdom") + ": **" + kingdomName + "**";

                    send("kingdoms.log.leave.title", body);
                    break;
                }

                case "org.kingdoms.events.lands.LandClaimEvent": {
                    Object playerObj = event.getClass().getMethod("getPlayer").invoke(event);
                    Object kingdom = event.getClass().getMethod("getKingdom").invoke(event);
                    Object land = event.getClass().getMethod("getLand").invoke(event);

                    String player = (String) playerObj.getClass().getMethod("getName").invoke(playerObj);
                    String kingdomName = (String) kingdom.getClass().getMethod("getName").invoke(kingdom);

                    Object loc = land.getClass().getMethod("getLocation").invoke(land);
                    int x = (int) loc.getClass().getMethod("getX").invoke(loc);
                    int z = (int) loc.getClass().getMethod("getZ").invoke(loc);
                    String world = (String) loc.getClass().getMethod("getWorld").invoke(loc);

                    String body =
                            LangManager.get(lang, "kingdoms.log.claim.kingdom") + ": **" + kingdomName + "**\n" +
                                    LangManager.get(lang, "kingdoms.log.claim.player") + ": **" + player + "**\n" +
                                    LangManager.get(lang, "kingdoms.log.claim.location") + ": **" + world + " @ " + x + ", " + z + "**";

                    send("kingdoms.log.claim.title", body);
                    break;
                }

                case "org.kingdoms.events.lands.LandUnclaimEvent": {
                    Object playerObj = event.getClass().getMethod("getPlayer").invoke(event);
                    Object kingdom = event.getClass().getMethod("getKingdom").invoke(event);
                    Object land = event.getClass().getMethod("getLand").invoke(event);

                    String player = (String) playerObj.getClass().getMethod("getName").invoke(playerObj);
                    String kingdomName = (String) kingdom.getClass().getMethod("getName").invoke(kingdom);

                    Object loc = land.getClass().getMethod("getLocation").invoke(land);
                    int x = (int) loc.getClass().getMethod("getX").invoke(loc);
                    int z = (int) loc.getClass().getMethod("getZ").invoke(loc);
                    String world = (String) loc.getClass().getMethod("getWorld").invoke(loc);

                    String body =
                            LangManager.get(lang, "kingdoms.log.unclaim.kingdom") + ": **" + kingdomName + "**\n" +
                                    LangManager.get(lang, "kingdoms.log.unclaim.player") + ": **" + player + "**\n" +
                                    LangManager.get(lang, "kingdoms.log.unclaim.location") + ": **" + world + " @ " + x + ", " + z + "**";

                    send("kingdoms.log.unclaim.title", body);
                    break;
                }

                case "org.kingdoms.events.relations.RelationChangeEvent": {
                    Object kingdom = event.getClass().getMethod("getKingdom").invoke(event);
                    Object other = event.getClass().getMethod("getOther").invoke(event);
                    Object newRel = event.getClass().getMethod("getNewRelation").invoke(event);

                    String kingdomName = (String) kingdom.getClass().getMethod("getName").invoke(kingdom);
                    String otherName = (String) other.getClass().getMethod("getName").invoke(other);
                    String relation = newRel.toString();

                    String body =
                            LangManager.get(lang, "kingdoms.log.relation.kingdom") + ": **" + kingdomName + "**\n" +
                                    LangManager.get(lang, "kingdoms.log.relation.other") + ": **" + otherName + "**\n" +
                                    LangManager.get(lang, "kingdoms.log.relation.relation") + ": **" + relation + "**";

                    send("kingdoms.log.relation.title", body);
                    break;
                }

                case "org.kingdoms.events.banks.BankDepositEvent": {
                    Object playerObj = event.getClass().getMethod("getPlayer").invoke(event);
                    Object kingdom = event.getClass().getMethod("getKingdom").invoke(event);
                    Object amount = event.getClass().getMethod("getAmount").invoke(event);

                    String player = (String) playerObj.getClass().getMethod("getName").invoke(playerObj);
                    String kingdomName = (String) kingdom.getClass().getMethod("getName").invoke(kingdom);

                    String body =
                            LangManager.get(lang, "kingdoms.log.bank.deposit.kingdom") + ": **" + kingdomName + "**\n" +
                                    LangManager.get(lang, "kingdoms.log.bank.deposit.player") + ": **" + player + "**\n" +
                                    LangManager.get(lang, "kingdoms.log.bank.deposit.amount") + ": **" + amount.toString() + "**";

                    send("kingdoms.log.bank.deposit.title", body);
                    break;
                }

                case "org.kingdoms.events.banks.BankWithdrawEvent": {
                    Object playerObj = event.getClass().getMethod("getPlayer").invoke(event);
                    Object kingdom = event.getClass().getMethod("getKingdom").invoke(event);
                    Object amount = event.getClass().getMethod("getAmount").invoke(event);

                    String player = (String) playerObj.getClass().getMethod("getName").invoke(playerObj);
                    String kingdomName = (String) kingdom.getClass().getMethod("getName").invoke(kingdom);

                    String body =
                            LangManager.get(lang, "kingdoms.log.bank.withdraw.kingdom") + ": **" + kingdomName + "**\n" +
                                    LangManager.get(lang, "kingdoms.log.bank.withdraw.player") + ": **" + player + "**\n" +
                                    LangManager.get(lang, "kingdoms.log.bank.withdraw.amount") + ": **" + amount.toString() + "**";

                    send("kingdoms.log.bank.withdraw.title", body);
                    break;
                }

                default:
                    break;
            }
        } catch (Exception ignored) {}
    }
}
