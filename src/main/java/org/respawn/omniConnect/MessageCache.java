package org.respawn.omniConnect;

import net.dv8tion.jda.api.entities.Message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Message cache - stores newly received messages for deletion/editing logging..
 */
public class MessageCache {

    private static final Map<String, Message> cache = new ConcurrentHashMap<>();

    /**
     * Store message in cache.
     *
     * @param message Discord message
     */
    public static void store(Message message) {
        cache.put(message.getId(), message);
    }

    /**
     * Retrieve a message based on its ID.
     *
     * @param id The message ID
     * @return The Message or null if not found
     */
    public static Message get(String id) {
        return cache.get(id);
    }

    /**
     * Remove message from cache.
     *
     * @param id Message ID
     */
    public static void remove(String id) {
        cache.remove(id);
    }
}
