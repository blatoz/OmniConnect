package org.respawn.omniConnect.ticket;

/**
 * Ticket típusok a jegyrendszerhez.
 * Minden típus egyedi azonosítóval, gomb szöveggel, leírással és csatorna prefixszel rendelkezik.
 */
public enum TicketType {

    /**
     * Általános támogatás és segítségkérés.
     */
    SUPPORT(
            "SUPPORT",
            "🎫 Support",
            "Általános segítségkérés.",
            "help"
    ),
    /**
     * Szabálysértés vagy játékos/üzenet jelentése.
     */
    REPORT(
            "REPORT",
            "🚨 Jelentés",
            "Szabálysértés, játékos vagy üzenet jelentése.",
            "report"
    ),
    /**
     * Szerver, plugin vagy Discord hibáinak jelentése.
     */
    BUG(
            "BUG",
            "🐞 Bug Report",
            "Hibák jelentése (szerver, plugin, Discord, stb.).",
            "bug"
    ),
    /**
     * Jelentkezés staff vagy csapat pozícióra.
     */
    TGF(
            "TGF",
            "📝 TGF",
            "Jelentkezés staff / csapat pozícióra.",
            "tgf"
    ),
    /**
     * Partnerségi megkeresések és együttműködések.
     */
    PARTNER(
            "PARTNER",
            "🤝 Partnerség",
            "Partnerségi megkeresések és együttműködések.",
            "partner"
    ),
    /**
     * Nyereményjáték jutalom átvétele.
     */
    REWARD(
            "REWARD",
            "🎁 Nyeremény átvétele",
            "Nyereményjáték jutalom átvétele.",
            "reward"
    ),
    /**
     * Elveszett tárgyak visszaigénylése.
     */
    LOST(
            "LOST",
            "📦 Elveszett cuccok",
            "Elveszett tárgyak visszaigénylése.",
            "lost"
    );

    private final String idSuffix;
    private final String buttonLabel;
    private final String description;
    private final String channelPrefix;

    /**
     * TicketType konstruktor.
     *
     * @param idSuffix ID suffix az azonosításhoz
     * @param buttonLabel Gomb szövege
     * @param description Leírás
     * @param channelPrefix Csatorna neve prefix
     */
    TicketType(String idSuffix, String buttonLabel, String description, String channelPrefix) {
        this.idSuffix = idSuffix;
        this.buttonLabel = buttonLabel;
        this.description = description;
        this.channelPrefix = channelPrefix;
    }

    /**
     * Az ID suffix lekérése.
     *
     * @return ID suffix
     */
    public String getIdSuffix() {
        return idSuffix;
    }

    /**
     * A gomb szövegének lekérése.
     *
     * @return Gomb szövege
     */
    public String getButtonLabel() {
        return buttonLabel;
    }

    /**
     * A leírás lekérése.
     *
     * @return Leírás
     */
    public String getDescription() {
        return description;
    }

    /**
     * A csatorna prefix lekérése.
     *
     * @return Csatorna prefix
     */
    public String getChannelPrefix() {
        return channelPrefix;
    }

    /**
     * A gomb teljes ID-jének lekérése.
     * Formátuma: {@code ticket:create:<ID_SUFFIX>}
     *
     * @return Teljes gomb ID
     */
    public String getCreateButtonId() {
        return "ticket:create:" + idSuffix;
    }

    /**
     * TicketType lekérése gomb ID alapján.
     *
     * @param buttonId A gomb ID-je (formátuma: ticket:create:XYZ)
     * @return A megfelelő TicketType vagy null, ha nem talál
     */
    public static TicketType fromButtonId(String buttonId) {
        if (buttonId == null) {
            return null;
        }
        // expected form: ticket:create:XYZ
        String[] parts = buttonId.split(":");
        if (parts.length != 3) {
            return null;
        }
        String suffix = parts[2];
        for (TicketType type : values()) {
            if (type.getIdSuffix().equalsIgnoreCase(suffix)) {
                return type;
            }
        }
        return null;
    }
}
