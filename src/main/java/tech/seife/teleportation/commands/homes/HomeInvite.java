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

public class HomeInvite implements CommandExecutor {

    private final Teleportation plugin;

    public HomeInvite(Teleportation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (Bukkit.getPlayer(args[0]) != null) {
                if (args.length == 1) {
                    invitationToSpecificHome("home", ((Player) sender).getDisplayName(), args[0]);
                } else if (args.length == 2 && args[1] != null) {
                    invitationToSpecificHome(args[1], ((Player) sender).getDisplayName(), args[0]);
                }
            }

        }
        return true;
    }

    private void invitationToSpecificHome(String homeName, String inviter, String invited) {
        if (inviter.equalsIgnoreCase(invited)) {
            Bukkit.getPlayer(inviter).sendMessage("You can't invite yourself to your own home.");
            return;
        }

        Player invitedToHome = Bukkit.getPlayer(invited);
        Player initiator = Bukkit.getPlayer(inviter);

        if (plugin.getDataHandler().getHandleData().isHomeValidUsername(initiator.getDisplayName(), homeName)) {
            plugin.getDataHandler().getHandleData().saveInvitation(initiator.getUniqueId().toString(), invitedToHome.getUniqueId().toString(), homeName);

            Map<ReplaceType, String> values = new HashMap<>();

            values.put(ReplaceType.PLAYER_NAME, invited);
            values.put(ReplaceType.HOME_NAME, homeName);

            initiator.sendMessage(MessageManager.getTranslatedMessageWithReplace(plugin, "toInvited", values));

            values.put(ReplaceType.PLAYER_NAME, inviter);
            invitedToHome.sendMessage(MessageManager.getTranslatedMessageWithReplace(plugin, "toInviter", values));
        }
    }
}
