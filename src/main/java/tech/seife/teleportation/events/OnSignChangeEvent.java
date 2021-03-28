package tech.seife.teleportation.events;

import tech.seife.teleportation.MessageManager;
import tech.seife.teleportation.Teleportation;
import tech.seife.teleportation.signs.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public final class OnSignChangeEvent implements Listener {

    private final Teleportation plugin;

    public OnSignChangeEvent(Teleportation plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChangeEvent(SignChangeEvent e) {
        if (e.getPlayer().hasPermission("Teleportation.createWarpSign") && plugin.getSignManager().isValidSign(e.getLines())) {
            Sign sign = new Sign(plugin.getDataHandler().getHandleData().getLatestIdOfSigns() + 1, e.getBlock().getLocation(),  plugin.getWarpManager().getWarp(e.getLine(3)).getLocation());

            plugin.getSignManager().addSign(sign.getId(), sign.getSignLocation(), sign.getLocationToTeleport());
            plugin.getDataHandler().getHandleData().saveSign(sign);
            e.getPlayer().sendMessage(MessageManager.getTranslatedMessage(plugin, "createdWarpSign"));
        }
    }

}
