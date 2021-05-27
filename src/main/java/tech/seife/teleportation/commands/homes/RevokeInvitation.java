package tech.seife.teleportation.commands.homes;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.seife.teleportation.MessageManager;
import tech.seife.teleportation.Teleportation;
import tech.seife.teleportation.enums.ReplaceType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RevokeInvitation implements CommandExecutor {

    private final Teleportation plugin;

    public RevokeInvitation(Teleportation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args.length == 2 && args[0] != null && args[1] != null && Bukkit.getPlayer(args[0]) != null) {
            if (Bukkit.getPlayer(args[0]) != null) {
                UUID invited = Bukkit.getPlayer(args[0]).getUniqueId();
                UUID inviter = ((Player) sender).getUniqueId();

                revokeInvitation(sender, args, invited, inviter);
            }
        }
        return true;
    }

    private void revokeInvitation(CommandSender sender, String[] args, UUID invited, UUID inviter) {
        if (plugin.getDataHandler().getHandleData().isValidInvitation(invited, inviter, args[1])) {

            Map<ReplaceType, String> values = new HashMap<>();

            values.put(ReplaceType.PLAYER_NAME, Bukkit.getPlayer(invited).getName());
            values.put(ReplaceType.HOME_NAME, args[1]);

            sender.sendMessage(MessageManager.getTranslatedMessageWithReplace(plugin, "revokeInvitationSender", values));

            if (Bukkit.getPlayer(invited).isOnline()) {
                values.put(ReplaceType.PLAYER_NAME, Bukkit.getPlayer(inviter).getName());

                Bukkit.getPlayer(invited).sendMessage(MessageManager.getTranslatedMessageWithReplace(plugin, "revokeInvitationInvited", values));
            }

            plugin.getDataHandler().getHandleData().removeInvitation(invited, inviter, args[1]);
        }
    }
}