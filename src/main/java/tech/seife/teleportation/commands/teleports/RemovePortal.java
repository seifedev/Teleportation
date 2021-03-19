package tech.seife.teleportation.commands.teleports;

import tech.seife.teleportation.Teleportation;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RemovePortal implements CommandExecutor {

    private final Teleportation plugin;

    public RemovePortal(Teleportation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0] != null) {
            if (!verifyLocationInConfig(args[0])) return true;

            Location location = plugin.getFileManager().getPortalsConfig().getConfigurationSection(args[0]).getLocation("portal");

            plugin.getDataHandler().getHandleData().removePortal(args[0]);
            plugin.getDataHolder().getPortalLocationWarp().remove(location);

            sender.sendMessage("portal removed");
        }
        return true;
    }

    private boolean verifyLocationInConfig(String name) {
        if (plugin.getFileManager() != null && plugin.getFileManager().getPortalsConfig() != null) {
            if (plugin.getFileManager().getPortalsConfig().getConfigurationSection(name) != null) {
                return plugin.getFileManager().getPortalsConfig().getConfigurationSection(name).getLocation("portal") != null;
            }
        }
        return false;
    }
}
