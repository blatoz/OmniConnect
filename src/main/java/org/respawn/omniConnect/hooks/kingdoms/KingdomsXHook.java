package org.respawn.omniConnect.hooks.kingdoms;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.respawn.omniConnect.Main;
import org.respawn.omniConnect.hooks.DiscordLog;

public class KingdomsXHook implements Listener {

    private final String pluginKey;

    public KingdomsXHook(String pluginKey) {
        this.pluginKey = pluginKey;
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onKingdomEvent(Event event) {
        String className = event.getClass().getName();

        try {
            switch (className) {
                // Kingdom létrehozás
                case "org.kingdoms.events.general.KingdomCreateEvent": {
                    Object creator = event.getClass().getMethod("getCreator").invoke(event);
                    Object kingdom = event.getClass().getMethod("getKingdom").invoke(event);

                    String playerName = (String) creator.getClass().getMethod("getName").invoke(creator);
                    String kingdomName = (String) kingdom.getClass().getMethod("getName").invoke(kingdom);

                    DiscordLog.send(pluginKey,
                            "🏰 Új Királyság Jött Létre",
                            "Játékos: **" + playerName + "**\nKirályság: **" + kingdomName + "**"
                    );
                    break;
                }

                // Kingdom disband
                case "org.kingdoms.events.general.KingdomDisbandEvent": {
                    Object executor = event.getClass().getMethod("getExecutor").invoke(event);
                    Object kingdom = event.getClass().getMethod("getKingdom").invoke(event);

                    String playerName = (String) executor.getClass().getMethod("getName").invoke(executor);
                    String kingdomName = (String) kingdom.getClass().getMethod("getName").invoke(kingdom);

                    DiscordLog.send(pluginKey,
                            "💥 Királyság Feloszlatva",
                            "Játékos: **" + playerName + "**\nKirályság: **" + kingdomName + "**"
                    );
                    break;
                }

                // Join
                case "org.kingdoms.events.members.KingdomJoinEvent": {
                    Object player = event.getClass().getMethod("getPlayer").invoke(event);
                    Object kingdom = event.getClass().getMethod("getKingdom").invoke(event);

                    String playerName = (String) player.getClass().getMethod("getName").invoke(player);
                    String kingdomName = (String) kingdom.getClass().getMethod("getName").invoke(kingdom);

                    DiscordLog.send(pluginKey,
                            "➕ Új Tag Csatlakozott",
                            "Játékos: **" + playerName + "**\nKirályság: **" + kingdomName + "**"
                    );
                    break;
                }

                // Leave
                case "org.kingdoms.events.members.KingdomLeaveEvent": {
                    Object player = event.getClass().getMethod("getPlayer").invoke(event);
                    Object kingdom = event.getClass().getMethod("getKingdom").invoke(event);

                    String playerName = (String) player.getClass().getMethod("getName").invoke(player);
                    String kingdomName = (String) kingdom.getClass().getMethod("getName").invoke(kingdom);

                    DiscordLog.send(pluginKey,
                            "➖ Tag Elhagyta a Királyságot",
                            "Játékos: **" + playerName + "**\nKirályság: **" + kingdomName + "**"
                    );
                    break;
                }

                // Claim
                case "org.kingdoms.events.lands.LandClaimEvent": {
                    Object player = event.getClass().getMethod("getPlayer").invoke(event);
                    Object kingdom = event.getClass().getMethod("getKingdom").invoke(event);
                    Object land = event.getClass().getMethod("getLand").invoke(event);

                    String playerName = (String) player.getClass().getMethod("getName").invoke(player);
                    String kingdomName = (String) kingdom.getClass().getMethod("getName").invoke(kingdom);

                    Object location = land.getClass().getMethod("getLocation").invoke(land);
                    int x = (int) location.getClass().getMethod("getX").invoke(location);
                    int z = (int) location.getClass().getMethod("getZ").invoke(location);
                    String world = (String) location.getClass().getMethod("getWorld").invoke(location);

                    DiscordLog.send(pluginKey,
                            "📦 Terület Lefoglalva",
                            "Királyság: **" + kingdomName + "**\nJátékos: **" + playerName + "**\n"
                                    + "Hely: **" + world + " @ " + x + ", " + z + "**"
                    );
                    break;
                }

                // Unclaim
                case "org.kingdoms.events.lands.LandUnclaimEvent": {
                    Object player = event.getClass().getMethod("getPlayer").invoke(event);
                    Object kingdom = event.getClass().getMethod("getKingdom").invoke(event);
                    Object land = event.getClass().getMethod("getLand").invoke(event);

                    String playerName = (String) player.getClass().getMethod("getName").invoke(player);
                    String kingdomName = (String) kingdom.getClass().getMethod("getName").invoke(kingdom);

                    Object location = land.getClass().getMethod("getLocation").invoke(land);
                    int x = (int) location.getClass().getMethod("getX").invoke(location);
                    int z = (int) location.getClass().getMethod("getZ").invoke(location);
                    String world = (String) location.getClass().getMethod("getWorld").invoke(location);

                    DiscordLog.send(pluginKey,
                            "📭 Terület Elengedve",
                            "Királyság: **" + kingdomName + "**\nJátékos: **" + playerName + "**\n"
                                    + "Hely: **" + world + " @ " + x + ", " + z + "**"
                    );
                    break;
                }

                // Diplomáciai változás
                case "org.kingdoms.events.relations.RelationChangeEvent": {
                    Object kingdom = event.getClass().getMethod("getKingdom").invoke(event);
                    Object other = event.getClass().getMethod("getOther").invoke(event);
                    Object newRel = event.getClass().getMethod("getNewRelation").invoke(event);

                    String kingdomName = (String) kingdom.getClass().getMethod("getName").invoke(kingdom);
                    String otherName = (String) other.getClass().getMethod("getName").invoke(other);
                    String relation = newRel.toString();

                    DiscordLog.send(pluginKey,
                            "⚖️ Diplomácia Változás",
                            "Királyság: **" + kingdomName + "**\nMásik: **" + otherName + "**\n"
                                    + "Új kapcsolat: **" + relation + "**"
                    );
                    break;
                }

                // Bank deposit
                case "org.kingdoms.events.banks.BankDepositEvent": {
                    Object player = event.getClass().getMethod("getPlayer").invoke(event);
                    Object kingdom = event.getClass().getMethod("getKingdom").invoke(event);
                    Object amount = event.getClass().getMethod("getAmount").invoke(event);

                    String playerName = (String) player.getClass().getMethod("getName").invoke(player);
                    String kingdomName = (String) kingdom.getClass().getMethod("getName").invoke(kingdom);

                    DiscordLog.send(pluginKey,
                            "💰 Bank Befizetés",
                            "Királyság: **" + kingdomName + "**\nJátékos: **" + playerName + "**\n"
                                    + "Összeg: **" + amount.toString() + "**"
                    );
                    break;
                }

                // Bank withdraw
                case "org.kingdoms.events.banks.BankWithdrawEvent": {
                    Object player = event.getClass().getMethod("getPlayer").invoke(event);
                    Object kingdom = event.getClass().getMethod("getKingdom").invoke(event);
                    Object amount = event.getClass().getMethod("getAmount").invoke(event);

                    String playerName = (String) player.getClass().getMethod("getName").invoke(player);
                    String kingdomName = (String) kingdom.getClass().getMethod("getName").invoke(kingdom);

                    DiscordLog.send(pluginKey,
                            "💸 Bank Kivét",
                            "Királyság: **" + kingdomName + "**\nJátékos: **" + playerName + "**\n"
                                    + "Összeg: **" + amount.toString() + "**"
                    );
                    break;
                }

                default:
                    break;
            }
        } catch (Exception ignored) {
        }
    }
}
