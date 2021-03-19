package tech.seife.teleportation.commands.teleports;

import tech.seife.teleportation.Teleportation;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LinkTeleportToBlock implements CommandExecutor {

    private final Teleportation plugin;

    public LinkTeleportToBlock(Teleportation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args.length == 2 && args[0] != null && args[1] != null) {
            if (plugin.getDataHolder().getGeneratedIdForWarps().get(Long.parseLong(args[0])) != null && plugin.getWarpManager().getWarp(args[1]) != null) {
                Location portalLocation = plugin.getDataHolder().getGeneratedIdForWarps().get(Long.parseLong(args[0]));
                Location warpLocation = plugin.getWarpManager().getWarp(args[1]).getLocation();

                if (portalLocation != null && warpLocation != null) {

                    plugin.getDataHolder().getPortalLocationWarp().put(portalLocation, warpLocation);
                    plugin.getDataHolder().getGeneratedIdForWarps().remove(Long.parseLong(args[0]));

                    plugin.getDataHandler().getHandleData().savePortals(args[1], portalLocation, warpLocation);
                    sender.sendMessage("Linked");
                } else {
                    sender.sendMessage("Failed");
                }
            }
        }
        return true;
    }
}
