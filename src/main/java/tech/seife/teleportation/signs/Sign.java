package tech.seife.teleportation.signs;

import org.bukkit.Location;

public final class Sign {

    private Location signLocation;
    private final Location locationToTeleport;
    private final int id;


    public Sign(int id, Location signLocation, Location locationToTeleport) {
        this.id = id;
        this.signLocation = signLocation;
        this.locationToTeleport = locationToTeleport;
    }

    public Location getSignLocation() {
        return signLocation;
    }

    public Location getLocationToTeleport() {
        return locationToTeleport;
    }

    public int getId() {
        return id;
    }
}
