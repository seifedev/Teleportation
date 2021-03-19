package tech.seife.teleportation.commands.homes;

import tech.seife.teleportation.Teleportation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Homes implements CommandExecutor {

    private final Teleportation plugin;

    public Homes(Teleportation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            sender.sendMessage("Your homes are: " + plugin.getDataHandler().getHandleData().getHomeNamesOfPlayer(((Player) sender).getUniqueId()).toString());
        }
        return true;
    }
}
