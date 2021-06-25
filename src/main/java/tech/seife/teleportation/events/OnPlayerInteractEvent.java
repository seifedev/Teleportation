package tech.seife.teleportation.events;

import tech.seife.teleportation.Teleportation;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class OnPlayerInteractEvent implements Listener {

    private final Teleportation plugin;

    public OnPlayerInteractEvent(Teleportation plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.getItem() != null && verifySelectorBlock(e.getItem())) {
                Long randomId = (long) (Math.random() * 3000L) + 1L;

                plugin.getDataHolder().setGeneratedIdForWarps(randomId, e.getClickedBlock().getLocation());

                e.getPlayer().sendMessage("the id of the clicked block is: " + randomId);
            } else if (e.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) e.getClickedBlock().getState();

                if (plugin.getSignManager().isValidSign(sign.getLines())) {
                    e.getPlayer().teleport(plugin.getWarpManager().getWarp(sign.getLine(2)).getLocation());
                }
            }
        }
    }

    public boolean verifySelectorBlock(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        NamespacedKey namespacedKey = new NamespacedKey(plugin, "BlockSelector");

        return meta.getPersistentDataContainer().has(namespacedKey, PersistentDataType.STRING);
    }
}
