package tech.seife.teleportation.datamanager;

import tech.seife.teleportation.Teleportation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.stream.Collectors;

public final class DataHolder {

    private final Map<UUID, LinkedHashSet<Location>> returnLocations;
    private final Map<UUID, Boolean> deleteWarpsVerification;
    private final Map<Long, Location> generatedIdForWarps;
    private final Map<Location, Location> portalLocationWarp;
    private final BukkitTask task;
    private Location spawnLocation;

    public DataHolder(Teleportation plugin) {
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
        return spawnLocation.clone();
    }

    public void setSpawnLocation(Location spawnLocation) {
        if (spawnLocation == null) {
            this.spawnLocation = getDefaultWorld(0);
        }
        this.spawnLocation = spawnLocation;
    }

    public Map<Long, Location> getGeneratedIdForWarps() {
        return Map.copyOf(generatedIdForWarps);
    }

    public void setGeneratedIdForWarps(long id, Location location) {
        if (location != null) {
            generatedIdForWarps.put(id, location);
        } else {
            generatedIdForWarps.put(id, getDefaultWorld(100));
        }
    }


    public Map<UUID, LinkedHashSet<Location>> getReturnLocations() {
        return Map.copyOf(returnLocations);
    }

    public void setReturnLocations(UUID playerUuid, Location location) {
        if (playerUuid != null) {
            LinkedHashSet<Location> locations;
            if (returnLocations.get(playerUuid) != null) {
                locations = returnLocations.get(playerUuid);
            } else {
                locations = new LinkedHashSet<>();
            }

            if (location != null) {
                locations.add(location);
            } else {
                locations.add(getDefaultWorld(100));
            }
        }
    }


    public Map<UUID, Boolean> getDeleteWarpsVerification() {
        return Map.copyOf(deleteWarpsVerification);
    }

    public void setDeleteWarpsVerification(UUID playerUuid, Boolean signal) {
        deleteWarpsVerification.put(playerUuid, signal);
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

    private Location getDefaultWorld(int i) {
        return new Location(Bukkit.getWorlds().get(0), 0, 100, i);
    }

}
