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
            dataHolder.setReturnLocations(e.getEntity().getUniqueId(), e.getEntity().getLocation());
        }
    }
}
