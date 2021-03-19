package tech.seife.teleportation.requestteleport;

import tech.seife.teleportation.Teleportation;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.scheduler.BukkitTask;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class RequesterTeleportManager {

    private final Teleportation plugin;

    private final int expireAfterInSeconds;

    private final Set<RequestTeleport> requests;

    private BukkitTask bukkitScheduler;

    public RequesterTeleportManager(Teleportation plugin, Configuration config) {
        this.plugin = plugin;
        requests = new HashSet<>();
        this.expireAfterInSeconds = config.getInt("tpaExpiration") > 1 ? config.getInt("tpaExpiration") : 30;
    }

    public void addRequest(UUID requester, UUID receiver, RequestTeleport.RequestType requestType) {
        requests.add(new RequestTeleport(requester, receiver, requestType, LocalDateTime.now().plusSeconds(expireAfterInSeconds)));
    }

    public void deleteRequests(UUID playerUuid) {
        if (getRequest(playerUuid) != null) {
            requests.remove(getRequest(playerUuid));
        }
    }

    public void intervalClearingRequests() {
        bukkitScheduler = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            requests.removeIf(requestTeleport -> requestTeleport.getTpaExpirationDate().isBefore(LocalDateTime.now()));
        }, 10, 30);
    }

    public RequestTeleport getRequest(UUID playerUuid) {
        return requests.stream()
                .filter(requestTeleport -> requestTeleport.getRequestReceiverUuid().equals(playerUuid) || requestTeleport.getRequestSenderUuid().equals(playerUuid))
                .findFirst()
                .orElse(null);

    }

    public BukkitTask getBukkitScheduler() {
        return bukkitScheduler;
    }
}
