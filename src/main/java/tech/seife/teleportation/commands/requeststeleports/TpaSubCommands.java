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
import java.util.UUID;

public final class TpaSubCommands implements CommandExecutor {

    private final Teleportation plugin;
    private final RequesterTeleportManager requesterTeleportManager;

    public TpaSubCommands(Teleportation plugin, RequesterTeleportManager requesterTeleportManager) {
        this.plugin = plugin;
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
        sendTeleportRequestMessages(sender, Bukkit.getPlayer(arg));
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

    private void sendTeleportRequestMessages(Player requester, Player receiver) {
        receiver.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessageManager().replacePlayerName(Objects.requireNonNull(plugin.getConfig().getString("tpaRequest")), requester.getName())));
        requester.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessageManager().replacePlayerName(Objects.requireNonNull(plugin.getConfig().getString("tpaRequestConfirm")), receiver.getName())));
    }


    private void sendDenyMessages(UUID requestSenderUuid, UUID requestReceiverUuid) {
        Bukkit.getPlayer(requestReceiverUuid).sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tpaCancelled")));
        Bukkit.getPlayer(requestSenderUuid).sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tpaCancelled")));
    }


    private void sendAcceptMessage(UUID firstPlayer, UUID secondPlayer) {
        Bukkit.getPlayer(firstPlayer).sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tpaSuccess")));
        Bukkit.getPlayer(secondPlayer).sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tpaSuccess")));
    }

}
