package tech.seife.teleportation.commands.requeststeleports;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.seife.teleportation.requestteleport.RequestTeleport;
import tech.seife.teleportation.requestteleport.RequesterTeleportManager;

public final class RequestTeleportHere implements CommandExecutor {

    private final RequesterTeleportManager requesterTeleportManager;

    public RequestTeleportHere(RequesterTeleportManager requesterTeleportManager) {
        this.requesterTeleportManager = requesterTeleportManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args.length == 1 && args[0] != null) {
            if (Bukkit.getPlayer(args[0]) != null) {
                requesterTeleportManager.addRequest(((Player) sender).getUniqueId(), Bukkit.getPlayer(args[0]).getUniqueId(), RequestTeleport.RequestType.TELEPORT_HERE);

                requesterTeleportManager.sendMessages(((Player) sender).getPlayer(), Bukkit.getPlayer(args[0]), "tpaRequest", "sendTpaHere");
            }
        }
        return true;
    }


}
