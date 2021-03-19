package tech.seife.teleportation.commands.teleports;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Teleport implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args.length == 1 && args[0] != null) {
            if (Bukkit.getPlayer(args[0]) != null) {
                ((Player) sender).teleport(Bukkit.getPlayer(args[0]).getLocation());
                return true;
            }
        }
        return true;
    }
}
