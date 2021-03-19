package tech.seife.teleportation.commands.homes;

import tech.seife.teleportation.Teleportation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class HomeInvite implements CommandExecutor {

    private final Teleportation plugin;

    public HomeInvite(Teleportation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args.length >= 1) {
            if (Bukkit.getPlayer(args[0]) != null) {
                if (args.length == 1) {
                    invitationToSpecificHome(sender, "home", args[0], ((Player) sender).getDisplayName());
                } else if (args.length == 2 && args[1] != null) {
                    invitationToSpecificHome(sender, args[1], args[0], ((Player) sender).getDisplayName());
                }
            }

        }
        return true;
    }

    private void invitationToSpecificHome(CommandSender sender, String homeName, String inviter, String invited) {
        if (plugin.getDataHandler().getHandleData().isHomeValidUsername(inviter, homeName)) {

            plugin.getDataHandler().getHandleData().saveInvitation(invited, inviter, homeName);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessageManager().replacePlayerName(Objects.requireNonNull(plugin.getConfig().getString("homeInvitationSender")), homeName)));
            Bukkit.getPlayer(invited).sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessageManager().replacePlayerName(Objects.requireNonNull(plugin.getConfig().getString("invitation")), homeName)));
        }
    }
}
