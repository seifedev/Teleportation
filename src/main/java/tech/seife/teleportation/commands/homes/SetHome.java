package tech.seife.teleportation.commands.homes;

import tech.seife.teleportation.Teleportation;
import tech.seife.teleportation.homes.Home;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                if (canCreateHome(player, args[0])) {
                    Location location = player.getLocation();

                    plugin.getDataHandler().getHandleData().saveHome(new Home(plugin.getDataHandler().getHandleData().getLatestIdOfHomes() + 1, player.getUniqueId(), player.getDisplayName(), args[0], location));

                    sendMessage(sender, args[0], "homeCreated");
                } else {
                    sendMessage(sender, args[0], "homeNotCreated");
                }
            }
        }
        return true;
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
        if (plugin.getMessageManager() != null && plugin.getConfig().getString(path) != null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessageManager().replaceHomeName(plugin.getConfig().getString(path), replaceName)));
        }
    }
}
