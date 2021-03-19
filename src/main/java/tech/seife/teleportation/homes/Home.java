package tech.seife.teleportation.homes;

import org.bukkit.Location;

import java.util.UUID;

public class Home {

    private final int id;
    private final UUID ownerUuid;
    private final String ownerName;
    private final String homeName;
    private final Location location;

    public Home(int id, UUID ownerUuid, String ownerName, String homeName, Location location) {
        this.id = id;
        this.ownerUuid = ownerUuid;
        this.ownerName = ownerName;
        this.homeName = homeName;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public UUID getOwnerUuid() {
        return ownerUuid;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getHomeName() {
        return homeName;
    }

    public Location getLocation() {
        return location;
    }
}
