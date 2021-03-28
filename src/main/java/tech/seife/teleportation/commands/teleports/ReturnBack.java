package tech.seife.teleportation.commands.teleports;

import tech.seife.teleportation.Teleportation;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReturnBack implements CommandExecutor {

    private final Teleportation plugin;

    public ReturnBack(Teleportation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender);
            if (plugin.getDataHolder().getReturnLocations().get(player.getUniqueId()) != null && !plugin.getDataHolder().getReturnLocations().get(player.getUniqueId()).isEmpty()) {
                Location location = getLocation(player);
                teleport(player, location);
                plugin.getDataHolder().getReturnLocations().get(player.getUniqueId()).remove(location);
                player.sendMessage(plugin.getFileManager().getTranslationConfig().getString("previousLocation"));
            } else {
                player.sendMessage(plugin.getFileManager().getTranslationConfig().getString("previousLocationNotFound"));
            }
            return true;
        }
        return true;
    }

    private void teleport(Player player, Location location) {
        player.teleport(location);
    }

    private Location getLocation(Player player) {
        return plugin.getDataHolder().getReturnLocations().get(player.getUniqueId()).iterator().next();
    }
}
