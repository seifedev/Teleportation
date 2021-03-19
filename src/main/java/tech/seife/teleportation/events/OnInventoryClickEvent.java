package tech.seife.teleportation.events;

import tech.seife.teleportation.datamanager.DataHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public final class OnInventoryClickEvent implements Listener {

    private final DataHolder dataHolder;

    public OnInventoryClickEvent(DataHolder dataHolder) {
        this.dataHolder = dataHolder;
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        if (e.getView().getTitle().equals("Verification Inventory")) {
            e.setCancelled(true);

            if (e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals("Agree")) {
                dataHolder.getDeleteWarpsVerification().put(e.getWhoClicked().getUniqueId(), true);
            } else if (e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().getDisplayName().equals("Disagree")) {
                dataHolder.getDeleteWarpsVerification().put(e.getWhoClicked().getUniqueId(), false);
            }
            e.getWhoClicked().closeInventory();
        }
    }
}
