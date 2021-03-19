package tech.seife.teleportation.commands.homes;

import tech.seife.teleportation.Teleportation;
import tech.seife.teleportation.homes.Home;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class GoHome implements CommandExecutor {

    private final Teleportation plugin;

    public GoHome(Teleportation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                teleportToDefaultHome(sender, player);
                return true;
            } else if (args.length == 1 && args[0] != null) {
                teleportToSpecificHome(sender, args[0], player);
                return true;
            }
        }
        return true;
    }

    private void teleportToSpecificHome(CommandSender sender, String homeName, Player player) {
        if (plugin.getDataHandler().getHandleData().isHomeValidUuid(player.getUniqueId(), homeName)) {
            Home home = plugin.getDataHandler().getHandleData().getHomeUuid(player.getUniqueId(), homeName);
            sender.sendMessage(home.getHomeName());
            teleport(sender, player, home);
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessageManager().replaceHomeName(Objects.requireNonNull(plugin.getConfig().getString("homeNotFound")), homeName)));
        }
    }

    private void teleportToDefaultHome(CommandSender sender, Player player) {
        if (plugin.getDataHandler().getHandleData().isHomeValidUuid(player.getUniqueId(), "home")) {
            Home home = plugin.getDataHandler().getHandleData().getHomeUuid(player.getUniqueId(), "home");
            teleport(sender, player, home);
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessageManager().replaceHomeName(Objects.requireNonNull(plugin.getConfig().getString("homeNotFound")), "default home")));
        }
    }

    private void teleport(CommandSender sender, Player player, Home home) {
        player.teleport(home.getLocation());
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessageManager().replaceHomeName(Objects.requireNonNull(plugin.getConfig().getString("homeMessage")), home.getHomeName())));
    }
}
