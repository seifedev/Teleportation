package tech.seife.teleportation.commands.homes;

import tech.seife.teleportation.Teleportation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RevokeInvitation implements CommandExecutor {

    private final Teleportation plugin;

    public RevokeInvitation(Teleportation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args.length == 2 && args[0] != null && args[1] != null) {
            if (Bukkit.getPlayer(args[0]) != null) {
                UUID invited = ((Player) sender).getUniqueId();
                UUID inviter = ((Player) sender).getUniqueId();

                revokeInvitation(sender, args, invited, inviter);
            }
        }
        return true;
    }

    private void revokeInvitation(CommandSender sender, String[] args, UUID invited, UUID inviter) {
        if (plugin.getDataHandler().getHandleData().isValidInvitation(invited, inviter, args[1])) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessageManager().replaceHomeName(plugin.getConfig().getString("revokeInvitation"), plugin.getDataHandler().getHandleData().getHomeUuid(inviter, args[1]).getHomeName())));
            plugin.getDataHandler().getHandleData().removeInvitation(invited, inviter, args[1]);
        }
    }
}