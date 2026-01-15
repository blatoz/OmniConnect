package org.respawn.omniConnect.ticket;

import org.respawn.omniConnect.lang.LangManager;

public enum TicketType {

    SUPPORT("support"),
    REPORT("report"),
    BUG("bug"),
    TGF("tgf"),
    PARTNER("partner"),
    REWARD("reward"),
    LOST("lost");

    private final String key;

    TicketType(String key) {
        this.key = key;
    }

    public String getLangKey() {
        return "discord.ticket.type." + key;
    }

    public String getButtonLabel() {
        String lang = LangManager.getDefaultLanguage();
        return LangManager.get(lang, getLangKey() + ".button");
    }

    public String getDescription() {
        String lang = LangManager.getDefaultLanguage();
        return LangManager.get(lang, getLangKey() + ".description");
    }

    public String getChannelPrefix() {
        String lang = LangManager.getDefaultLanguage();
        return LangManager.get(lang, getLangKey() + ".prefix");
    }

    public String getCreateButtonId() {
        return "ticket:create:" + key.toUpperCase();
    }

    public static TicketType fromButtonId(String buttonId) {
        if (buttonId == null) return null;

        String[] parts = buttonId.split(":");
        if (parts.length != 3) return null;

        String suffix = parts[2].toLowerCase();

        for (TicketType type : values()) {
            if (type.key.equalsIgnoreCase(suffix)) {
                return type;
            }
        }
        return null;
    }
}
