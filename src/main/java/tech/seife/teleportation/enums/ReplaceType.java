package tech.seife.teleportation.enums;

public enum ReplaceType {
    PLAYER_NAME("%player%"),
    HOME_NAME("%home%"),
    WARP_NAME("%warp%");

    private final String value;

    ReplaceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
