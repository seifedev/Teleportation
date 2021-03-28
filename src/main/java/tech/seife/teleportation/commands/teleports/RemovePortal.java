package tech.seife.teleportation.commands.teleports;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tech.seife.teleportation.MessageManager;
import tech.seife.teleportation.Teleportation;

public class RemovePortal implements CommandExecutor {

    private final Teleportation plugin;

    public RemovePortal(Teleportation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0] != null) {
            if (!verifyLocationInConfig(args[0])) return true;

            plugin.getDataHandler().getHandleData().removePortal(args[0]);
            plugin.getDataHandler().getHandleData().loadPortals();

            sender.sendMessage(MessageManager.getTranslatedMessage(plugin, "removedPortal"));
        }
        return true;
    }

    private boolean verifyLocationInConfig(String name) {
        return plugin.getDataHandler().getHandleData().doesPortalExist(name);
    }
}
