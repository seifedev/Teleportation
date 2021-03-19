package tech.seife.teleportation.warps;

import org.bukkit.Location;

public final class Warp {

    private final int id;
    private final Location location;
    private final String name;

    public Warp(int id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
}
