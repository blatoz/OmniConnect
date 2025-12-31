package org.respawn.omniConnect;

import net.dv8tion.jda.api.entities.Message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Üzenet cache - tárola az újonnan fogadott üzeneteket a törlés/szerkesztés logolásához.
 */
public class MessageCache {

    private static final Map<String, Message> cache = new ConcurrentHashMap<>();

    /**
     * Üzenet tárolása a cache-ben.
     *
     * @param message A Discord üzenet
     */
    public static void store(Message message) {
        cache.put(message.getId(), message);
    }

    /**
     * Üzenet lekérése ID alapján.
     *
     * @param id Az üzenet ID-je
     * @return A Message vagy null, ha nem található
     */
    public static Message get(String id) {
        return cache.get(id);
    }

    /**
     * Üzenet eltávolítása a cache-ből.
     *
     * @param id Az üzenet ID-je
     */
    public static void remove(String id) {
        cache.remove(id);
    }
}
