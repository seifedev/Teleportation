package tech.seife.teleportation.commands.requeststeleports;

import tech.seife.teleportation.Teleportation;
import tech.seife.teleportation.requestteleport.RequestTeleport;
import tech.seife.teleportation.requestteleport.RequesterTeleportManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public final class RequestTeleportThere implements CommandExecutor {

    private final RequesterTeleportManager requesterTeleportManager;
    private final Teleportation plugin;

    public RequestTeleportThere(Teleportation plugin, RequesterTeleportManager requesterTeleportManager) {
        this.plugin = plugin;
        this.requesterTeleportManager = requesterTeleportManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args.length == 1 && args[0] != null) {
            if (Bukkit.getPlayer(args[0]) != null) {
                requesterTeleportManager.addRequest(((Player) sender).getUniqueId(), Bukkit.getPlayer(args[0]).getUniqueId(), RequestTeleport.RequestType.TELEPORT_THERE);

                requesterTeleportManager.sendMessages(((Player) sender).getPlayer(), Bukkit.getPlayer(args[0]), "tpaRequest", "sendHere");
            }
        }
        return true;
    }

}
