package tech.seife.teleportation.commands.requeststeleports;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.seife.teleportation.requestteleport.RequestTeleport;
import tech.seife.teleportation.requestteleport.RequesterTeleportManager;

import java.util.UUID;

public final class TpaSubCommands implements CommandExecutor {

    private final RequesterTeleportManager requesterTeleportManager;

    public TpaSubCommands(RequesterTeleportManager requesterTeleportManager) {
        this.requesterTeleportManager = requesterTeleportManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) return true;

        if (sender instanceof Player && sender.hasPermission("teleportation.tpa")) {
            Player player = (Player) sender;

            if (Bukkit.getPlayer(args[0]) != null) {
                requestTeleport(player, args[0]);
            } else if (args[0].equalsIgnoreCase("accept")) {
                teleportPlayer(player);
            } else if (args[0].equalsIgnoreCase("deny")) {
                cancelTeleportRequest((Player) sender);
            }
            return true;

        }


        return true;
    }

    private void requestTeleport(Player sender, String arg) {
        requesterTeleportManager.addRequest(sender.getUniqueId(), Bukkit.getPlayer(arg).getUniqueId(), RequestTeleport.RequestType.TELEPORT_THERE);
        requesterTeleportManager.sendMessages(sender.getPlayer(), Bukkit.getPlayer(arg), "tpaRequest", "sendHere");
    }

    private void cancelTeleportRequest(Player sender) {
        sendDenyMessages(requesterTeleportManager.getRequest(sender.getUniqueId()).getRequestSenderUuid(), requesterTeleportManager.getRequest(sender.getUniqueId()).getRequestReceiverUuid());
        requesterTeleportManager.deleteRequests(sender.getUniqueId());
    }

    private void teleportPlayer(Player player) {
        RequestTeleport requestTeleport = requesterTeleportManager.getRequest(player.getUniqueId());
        if (requestTeleport != null) {
            if (requestTeleport.getRequestType() == RequestTeleport.RequestType.TELEPORT_HERE) {
                Bukkit.getPlayer(requestTeleport.getRequestReceiverUuid()).teleport(Bukkit.getPlayer(requestTeleport.getRequestSenderUuid()).getLocation());
            } else if (requestTeleport.getRequestType() == RequestTeleport.RequestType.TELEPORT_THERE) {
                Bukkit.getPlayer(requestTeleport.getRequestSenderUuid()).teleport(Bukkit.getPlayer(requestTeleport.getRequestReceiverUuid()).getLocation());
            }
            sendAcceptMessage(requestTeleport.getRequestSenderUuid(), requestTeleport.getRequestReceiverUuid());
            requesterTeleportManager.deleteRequests(player.getUniqueId());
        }
    }


    private void sendDenyMessages(UUID requestSenderUuid, UUID requestReceiverUuid) {
        requesterTeleportManager.sendMessages(Bukkit.getPlayer(requestSenderUuid).getPlayer(), Bukkit.getPlayer(requestReceiverUuid).getPlayer(), "cancelledTpaSender", "cancelledTpaReceiver");
    }


    private void sendAcceptMessage(UUID firstPlayer, UUID secondPlayer) {
        requesterTeleportManager.sendMessages(Bukkit.getPlayer(firstPlayer).getPlayer(), Bukkit.getPlayer(secondPlayer).getPlayer(), "tpaSuccessSender", "tpaSuccessReceiver");
    }

}
