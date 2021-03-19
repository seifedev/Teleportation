package tech.seife.teleportation.commands.teleports;

import tech.seife.teleportation.Teleportation;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawn implements CommandExecutor {

    private final Teleportation plugin;

    public SetSpawn(Teleportation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            Location location = player.getLocation().clone();

            location.setY(location.getY() + 1);

            plugin.getDataHandler().getHandleData().saveSpawnLocation(location);
            plugin.getDataHolder().setSpawnLocation(location);
        }
        return true;
    }
}
