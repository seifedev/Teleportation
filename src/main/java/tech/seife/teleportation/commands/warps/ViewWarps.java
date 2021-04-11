package tech.seife.teleportation.commands.warps;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tech.seife.teleportation.MessageManager;
import tech.seife.teleportation.Teleportation;
import tech.seife.teleportation.warps.Warp;

import java.util.ArrayList;
import java.util.List;

public class ViewWarps implements CommandExecutor {

    private final Teleportation plugin;

    public ViewWarps(Teleportation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0] != null) {
            int page = 0;

            page = parsePage(args, page);

            sendFirstPage(sender);
            if (plugin.getWarpManager().getWarps().size() >= page * 10) {
                sendWarpsToChat(sender, page);
                return true;
            }
        } else {
            sendFirstPage(sender);
            return true;
        }
        return true;
    }

    private void sendWarpsToChat(CommandSender sender, int page) {
        List<Warp> warps = new ArrayList<>(plugin.getWarpManager().getWarps());

        int index = 0;
        for (int i = page * 10; i <= warps.size(); i++) {
            sender.sendMessage(warps.get(i).getName());
            index++;

            if (index == 10) break;
        }
    }

    private int parsePage(String[] args, int page) {
        try {
            page = Integer.parseInt(args[0]);
            if (page == 0) page++;
        } catch (NumberFormatException e) {
            Bukkit.getLogger().warning(e.getMessage());
        }
        return page;
    }

    private void sendFirstPage(CommandSender sender) {
        if (plugin.getWarpManager().getWarps().size() == 0) {
            sender.sendMessage(MessageManager.getTranslatedMessage(plugin, "noWarpsAvailable"));
        } else if (plugin.getWarpManager().getWarps().size() <= 10) {
            for (Warp warp : plugin.getWarpManager().getWarps()) {
                sender.sendMessage(warp.getName() + ",");
            }
        }
    }
}
