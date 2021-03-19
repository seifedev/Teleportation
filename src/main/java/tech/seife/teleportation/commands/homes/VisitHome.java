package tech.seife.teleportation.commands.homes;

import tech.seife.teleportation.Teleportation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class VisitHome implements CommandExecutor {

    private final Teleportation plugin;

    public VisitHome(Teleportation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args.length == 2 && args[0] != null && args[1] != null) {
            if (Bukkit.getPlayer(args[0]) != null) {
                UUID invited = ((Player) sender).getUniqueId();
                UUID inviter = Bukkit.getPlayer(args[0]).getUniqueId();
                if (plugin.getDataHandler().getHandleData().isValidInvitation(invited, inviter, args[1])) {
                    Bukkit.getPlayer(invited).teleport(plugin.getDataHandler().getHandleData().getHomeUuid(inviter, args[1]).getLocation());
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessageManager().replaceHomeName(plugin.getConfig().getString("homeVisit"), plugin.getDataHandler().getHandleData().getHomeUuid(inviter, args[1]).getHomeName())));
                }
            }
        }
        return true;
    }
}
