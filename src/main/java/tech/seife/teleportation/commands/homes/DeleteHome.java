package tech.seife.teleportation.commands.homes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.seife.teleportation.MessageManager;
import tech.seife.teleportation.Teleportation;
import tech.seife.teleportation.enums.ReplaceType;
import tech.seife.teleportation.homes.Home;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DeleteHome implements CommandExecutor {

    private final Teleportation plugin;

    public DeleteHome(Teleportation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            UUID playerUuid = ((Player) sender).getUniqueId();
            if (args.length == 0 && plugin.getDataHandler().getHandleData().isHomeValidUuid(playerUuid, "home")) {
                removeHome(sender, playerUuid, "home");
                return true;
            } else if (args.length == 1 && args[0] != null && plugin.getDataHandler().getHandleData().isHomeValidUuid(playerUuid, args[0])) {
                removeHome(sender, playerUuid, args[0]);
                return true;
            }
        }
        return true;
    }

    private void removeHome(CommandSender sender, UUID playerUuid, String homeName) {
        Home home = plugin.getDataHandler().getHandleData().getHomeUuid(playerUuid, homeName);
        plugin.getDataHandler().getHandleData().removeHome(plugin.getDataHandler().getHandleData().getHomeUuid(playerUuid, homeName));
        plugin.getDataHandler().getHandleData().removeHome(home);

        Map<ReplaceType, String> values = new HashMap<>();

        values.put(ReplaceType.HOME_NAME, homeName);

        sender.sendMessage(MessageManager.getTranslatedMessageWithReplace(plugin, "deleteHome", values));
    }
}
