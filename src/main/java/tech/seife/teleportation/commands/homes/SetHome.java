package tech.seife.teleportation.commands.homes;

import org.bukkit.Location;
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

public class SetHome implements CommandExecutor {

    private final Teleportation plugin;

    public SetHome(Teleportation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1 && args[0] != null) {
                extracted(sender, args[0], player);
            } else if (args.length == 0) {
                extracted(sender, "home", player);
            } else {
                sender.sendMessage(MessageManager.getTranslatedMessage(plugin, "wrongAmountOfArguments"));
            }
        }
        return true;
    }

    private void extracted(CommandSender sender, String homeName, Player player) {
        if (canCreateHome(player, homeName)) {
            Location location = player.getLocation();

            plugin.getDataHandler().getHandleData().saveHome(new Home(plugin.getDataHandler().getHandleData().getLatestIdOfHomes() + 1, player.getUniqueId(), player.getDisplayName(), homeName, location));

            sendMessage(sender, homeName, "homeCreated");
        } else {
            sendMessage(sender, homeName, "homeNotCreated");
        }
    }

    private boolean canCreateHome(Player player, String homeName) {
        for (int i = 0; i <= plugin.getConfig().getInt("maxHomes"); i++) {
            if (player.hasPermission("Teleportation.homes." + i)) {
                if (plugin.getDataHandler().getHandleData().getNumberOfHomes(player.getUniqueId()) < i) {
                    return !plugin.getDataHandler().getHandleData().isHomeValidUuid(player.getUniqueId(), homeName);
                }
            }
        }

        return false;
    }

    private void sendMessage(CommandSender sender, String replaceName, String path) {

        Map<ReplaceType, String> values = new HashMap<>();

        values.put(ReplaceType.HOME_NAME, replaceName);

        sender.sendMessage(MessageManager.getTranslatedMessageWithReplace(plugin, path, values));
    }
}
