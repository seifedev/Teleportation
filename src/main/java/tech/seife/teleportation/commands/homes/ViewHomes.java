package tech.seife.teleportation.commands.homes;

import tech.seife.teleportation.Teleportation;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ViewHomes implements CommandExecutor {

    private final Teleportation plugin;

    public ViewHomes(Teleportation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0] != null && Bukkit.getPlayer(args[0]) != null) {
            Player player = Bukkit.getPlayer(args[0]);
            if (plugin.getDataHandler().getHandleData().getHomeNamesOfPlayer(player.getUniqueId()) != null) {
                List<String> homeNames = plugin.getDataHandler().getHandleData().getHomeNamesOfPlayer(player.getUniqueId());
                sender.sendMessage(homeNames.toString());
                return true;
            }
        }
        return true;
    }
}
