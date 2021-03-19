package tech.seife.teleportation.commands.homes;

import tech.seife.teleportation.Teleportation;
import tech.seife.teleportation.homes.Home;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;
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
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessageManager().replaceHomeName(Objects.requireNonNull(plugin.getConfig().getString("homeDelete")), home.getHomeName())));
    }
}
