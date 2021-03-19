package tech.seife.teleportation.datamanager;

import tech.seife.teleportation.Teleportation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public final class DataHolder {

    private final Map<UUID, Map<UUID, Integer>> teleportRequest;
    private final Map<UUID, LinkedHashSet<Location>> returnLocations;
    private final Map<UUID, Boolean> deleteWarpsVerification;
    private final Map<Long, Location> generatedIdForWarps;
    private final Map<Location, Location> portalLocationWarp;
    private final BukkitTask task;
    private Location spawnLocation;

    public DataHolder(Teleportation plugin) {
        teleportRequest = new HashMap<>();
        returnLocations = new HashMap<>();
        deleteWarpsVerification = new HashMap<>();
        generatedIdForWarps = new HashMap<>();
        portalLocationWarp = new HashMap<>();
        spawnLocation = null;
        task = new SavePlayerLocation().runTaskTimerAsynchronously(plugin, 0L, 72000L);
    }

    public Map<Location, Location> getPortalLocationWarp() {
        return portalLocationWarp;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public Map<Long, Location> getGeneratedIdForWarps() {
        return generatedIdForWarps;
    }

    public Map<UUID, Map<UUID, Integer>> getTeleportRequest() {
        return teleportRequest;
    }

    public Map<UUID, LinkedHashSet<Location>> getReturnLocations() {
        return returnLocations;
    }

    public Map<UUID, Boolean> getDeleteWarpsVerification() {
        return deleteWarpsVerification;
    }

    public BukkitTask getTasks() {
        return task;
    }

    private class SavePlayerLocation extends BukkitRunnable {
        @Override
        public void run() {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                if (returnLocations.get(player.getUniqueId()) != null) {
                    LinkedHashSet<Location> linkedHashSet = returnLocations.get(player.getUniqueId());
                    linkedHashSet.add(player.getLocation());
                    returnLocations.put(player.getUniqueId(), linkedHashSet);
                } else {
                    LinkedHashSet<Location> linkedHashSet = new LinkedHashSet<>();
                    linkedHashSet.add(player.getLocation());
                    returnLocations.put(player.getUniqueId(), linkedHashSet);
                }
            }
        }
    }
}
