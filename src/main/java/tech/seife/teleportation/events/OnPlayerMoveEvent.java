package tech.seife.teleportation.events;

import tech.seife.teleportation.Teleportation;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class OnPlayerMoveEvent implements Listener {

    private final Teleportation plugin;

    public OnPlayerMoveEvent(Teleportation plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e) {
        if (plugin.getDataHolder() != null && plugin.getDataHolder().getPortalLocationWarp() != null) {
            plugin.getDataHolder().getPortalLocationWarp().forEach((portal, warpPoint) -> {
                if (verifyLocation(e.getTo(), portal)) {
                    e.getPlayer().teleport(warpPoint);
                }
            });
        }
    }

    private boolean verifyLocation(Location firstLocation, Location secondLocation) {
        return firstLocation.getBlockX() == secondLocation.getBlockX() && firstLocation.getBlockY() == secondLocation.getBlockY() && firstLocation.getBlockZ() == secondLocation.getBlockZ();
    }
}
