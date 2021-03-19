package tech.seife.teleportation.commands.warps;

import tech.seife.teleportation.Teleportation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpTeleport implements CommandExecutor {


    private final Teleportation plugin;

    public WarpTeleport(Teleportation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0] != null) {
            if (sender.hasPermission("Teleportation.warp." + args[0]) && plugin.getWarpManager().getWarp(args[0]) != null) {
                ((Player) sender).teleport(plugin.getWarpManager().getWarp(args[0]).getLocation());
                return true;
            }
        }
        return true;
    }
}
