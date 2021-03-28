package tech.seife.teleportation.commands.homes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.seife.teleportation.MessageManager;
import tech.seife.teleportation.Teleportation;
import tech.seife.teleportation.enums.ReplaceType;
import tech.seife.teleportation.homes.Home;

import java.util.HashMap;
import java.util.Map;

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

            teleport(sender, player, home);
        } else {
            homeNotFound(sender, homeName, "homeNotFound");
        }
    }

    private void teleportToDefaultHome(CommandSender sender, Player player) {
        if (plugin.getDataHandler().getHandleData().isHomeValidUuid(player.getUniqueId(), "home")) {
            Home home = plugin.getDataHandler().getHandleData().getHomeUuid(player.getUniqueId(), "home");
            teleport(sender, player, home);
        } else {
            homeNotFound(sender, "home", "homeNotFound");
        }
    }

    private void homeNotFound(CommandSender sender, String home, String path) {
        Map<ReplaceType, String> values = new HashMap<>();

        values.put(ReplaceType.HOME_NAME, home);

        sendMessage(sender, values, path);
    }

    private void teleport(CommandSender sender, Player player, Home home) {
        player.teleport(home.getLocation());

        Map<ReplaceType, String> values = new HashMap<>();

        values.put(ReplaceType.HOME_NAME, home.getHomeName());

        sendMessage(sender, values, "home");
    }

    private void sendMessage(CommandSender sender, Map<ReplaceType, String> values, String path) {
        sender.sendMessage(MessageManager.getTranslatedMessageWithReplace(plugin, path, values));
    }
}
