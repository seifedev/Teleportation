package tech.seife.teleportation.commands.warps;

import tech.seife.teleportation.Teleportation;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class BlockSelector implements CommandExecutor {

    private final Teleportation plugin;

    public BlockSelector(Teleportation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            ((Player) sender).getInventory().addItem(getBlockSelectorItem());
        }
        return true;
    }

    private ItemStack getBlockSelectorItem() {
        NamespacedKey namespacedKey = new NamespacedKey(plugin, "BlockSelector");

        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();

        meta.getPersistentDataContainer().set(namespacedKey, PersistentDataType.STRING, "BlockSelector");

        item.setItemMeta(meta);
        return item;
    }
}
