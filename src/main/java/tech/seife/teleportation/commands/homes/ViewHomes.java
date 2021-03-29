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
import java.util.List;
import java.util.Map;

public class ViewHomes implements CommandExecutor {

    private final Teleportation plugin;

    public ViewHomes(Teleportation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0] != null && Bukkit.getPlayer(args[0]) != null) {
            Player player = Bukkit.getPlayer(args[0]);
            if (plugin.getDataHandler().getHandleData().getHomeNamesOfPlayer(player.getUniqueId()) != null) {
                List<String> homeNames = plugin.getDataHandler().getHandleData().getHomeNamesOfPlayer(player.getUniqueId());

                if (homeNames.isEmpty()) {
                    Map<ReplaceType, String> values = new HashMap<>();
                    values.put(ReplaceType.PLAYER_NAME, player.getName());
                    
                    MessageManager.getTranslatedMessageWithReplace(plugin, "playerDoesntHaveAHouse", values);

                } else {
                    sender.sendMessage(homeNames.toString());
                }
                return true;
            }
        }
        return true;
    }
}
