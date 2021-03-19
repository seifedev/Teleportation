package tech.seife.teleportation.commands.teleports;

import tech.seife.teleportation.Teleportation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RandomTeleport implements CommandExecutor {

    private final Teleportation plugin;

    public RandomTeleport(Teleportation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                Player player = ((Player) sender);
                Location location = getRandomLocation(player.getWorld(), player.getWorld().getWorldBorder().getSize() / 2);
                Bukkit.getScheduler().runTask(plugin, () -> {
                    player.teleport(location);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("randomTeleport")));
                });
            });
            return true;
        }
        return true;
    }

    private Location getRandomLocation(World world, double borderSize) {
        Location location = null;
        int x = 0;
        int z = 0;

        do {
            x = (int) (Math.random() * borderSize) + 1;
            z = (int) (Math.random() * borderSize) + 1;
            location = new Location(world, x, world.getHighestBlockAt(x, z).getY(), z);
        } while (!isSafeLocation(location));

        location.setY(location.getY() + 1);
        return location;
    }

    public boolean isSafeLocation(Location location) {
        Block feet = location.getBlock();
        if (!feet.getType().isTransparent() && !feet.getLocation().add(0, 1, 0).getBlock().getType().isTransparent()) {
            return false;
        }

        Block head = feet.getRelative(BlockFace.UP);
        if (!head.getType().isTransparent()) {
            return false;
        }

        Block ground = feet.getRelative(BlockFace.DOWN);
        if (!ground.getType().isSolid()) {
            return false;
        }
        return true;
    }
}
