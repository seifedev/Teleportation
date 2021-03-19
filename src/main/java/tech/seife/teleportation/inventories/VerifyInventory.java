package tech.seife.teleportation.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class VerifyInventory {

    private final Inventory verifyInventory;

    public VerifyInventory(Player inventoryHolder) {
        verifyInventory = Bukkit.createInventory(inventoryHolder, 9, "Verification Inventory");
        populateInventory();
    }

    private void populateInventory() {
        addItemToInventory(Material.GREEN_WOOL, "Agree", 3);
        addItemToInventory(Material.RED_WOOL, "Disagree", 4);
    }

    private void addItemToInventory(Material material, String name, int slot) {
        ItemStack item = new ItemStack(material);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);

        item.setItemMeta(meta);

        verifyInventory.setItem(slot, item);
    }

    public void openInventory(Player player) {
        player.openInventory(verifyInventory);
    }
}
