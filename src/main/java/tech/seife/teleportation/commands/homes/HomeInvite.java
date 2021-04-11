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
                    invitationToSpecificHome(sender, "home", args[0], ((Player) sender).getDisplayName());
                } else if (args.length == 2 && args[1] != null) {
                    invitationToSpecificHome(sender, args[1], args[0], ((Player) sender).getDisplayName());
                }
            }

        }
        return true;
    }

    private void invitationToSpecificHome(CommandSender sender, String homeName, String inviter, String invited) {
        if (invited.equals(((Player) sender).getUniqueId().toString())) {
            sender.sendMessage("You can't invite yourself to your own home.");
            return;
        }
        if (plugin.getDataHandler().getHandleData().isHomeValidUsername(inviter, homeName)) {

            plugin.getDataHandler().getHandleData().saveInvitation(invited, inviter, homeName);

            Map<ReplaceType, String> values = new HashMap<>();

            values.put(ReplaceType.PLAYER_NAME, invited);
            values.put(ReplaceType.HOME_NAME, homeName);

            sender.sendMessage(MessageManager.getTranslatedMessageWithReplace(plugin, "invitedToHome", values));

            values.put(ReplaceType.PLAYER_NAME, inviter);

            Bukkit.getPlayer(invited).sendMessage(MessageManager.getTranslatedMessageWithReplace(plugin, "invitePlayer", values));
        }
    }
}
