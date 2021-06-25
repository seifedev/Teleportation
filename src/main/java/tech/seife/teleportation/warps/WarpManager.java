package tech.seife.teleportation.warps;

import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

public final class WarpManager {

    private final Set<Warp> warps;
    private int id;

    public WarpManager() {
        warps = new HashSet<>();
        id = -1;
    }

    public void addWarp(Warp warp) {
        id = warp.getId() + 1;
        warps.add(warp);
    }

    public void addWarp(String warpName, Location location) {
        id = generateId();
        addWarp(new Warp(id, warpName, location));
    }

    public void removeWarp(String warpName) {
        warps.removeIf(warp -> warp.getName().equals(warpName));
    }

    public Warp getWarp(String warpName) {
        return warps.stream()
                .filter(warp -> warp.getName().equals(warpName))
                .findFirst()
                .orElse(null);
    }


    private int generateId() {
        return id != -1 ? 0 : ++id;
    }


    public Set<Warp> getWarps() {
        return warps;
    }
}
