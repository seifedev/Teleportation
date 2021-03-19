package tech.seife.teleportation.events;

import tech.seife.teleportation.Teleportation;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class OnPlayerJoinEvent implements Listener {

    private final Teleportation plugin;

    public OnPlayerJoinEvent(Teleportation plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
    }
}
