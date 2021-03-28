package tech.seife.teleportation.commands.teleports;

import tech.seife.teleportation.MessageManager;
import tech.seife.teleportation.Teleportation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn implements CommandExecutor {

    private final Teleportation plugin;

    public Spawn(Teleportation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (plugin.getDataHolder().getSpawnLocation() != null) {
                ((Player) sender).teleport(plugin.getDataHolder().getSpawnLocation());
                MessageManager.getTranslatedMessage(plugin, "spawn");
            }
        }
        return true;
    }
}
