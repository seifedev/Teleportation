package tech.seife.teleportation.events;

import tech.seife.teleportation.datamanager.DataHolder;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.LinkedHashSet;

public class OnPlayerDeathEvent implements Listener {

    private final DataHolder dataHolder;

    public OnPlayerDeathEvent(DataHolder dataHolder) {
        this.dataHolder = dataHolder;
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent e) {
        if (dataHolder.getReturnLocations().get(e.getEntity().getUniqueId()) != null) {
            LinkedHashSet<Location> locations = dataHolder.getReturnLocations().get(e.getEntity().getUniqueId());

            locations.add(e.getEntity().getLocation());
        } else {
            LinkedHashSet<Location> locations = new LinkedHashSet<>();
            locations.add(e.getEntity().getLocation());

            dataHolder.getReturnLocations().put(e.getEntity().getUniqueId(), locations);
        }
    }
}
