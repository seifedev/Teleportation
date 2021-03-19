package tech.seife.teleportation.commands.warps;

import tech.seife.teleportation.Teleportation;
import tech.seife.teleportation.inventories.VerifyInventory;
import tech.seife.teleportation.warps.Warp;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteWarp implements CommandExecutor {

    private final Teleportation plugin;

    public DeleteWarp(Teleportation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        if (args.length == 1 && args[0] != null) {
            if (plugin.getWarpManager().getWarp(args[0]) != null) {
                Warp warp = plugin.getWarpManager().getWarp(args[0]);
                deleteWrap(((Player) sender), warp);
                if (plugin.getMessageManager() != null && plugin.getConfig().getString("warpDelete") != null) {
                    sender.sendMessage(plugin.getMessageManager().replaceWarpPoint(plugin.getConfig().getString("warpDelete"), warp.getLocation(), warp.getName()));
                }
                return true;
            }
        }
        return true;
    }

    private void deleteWrap(Player warpDeleter, Warp warp) {
        sendVerificationProcess(warpDeleter);
        deleteWarpIfPossible(warpDeleter, warp);
    }

    private void sendVerificationProcess(Player warpDeleter) {
        plugin.getDataHolder().getDeleteWarpsVerification().put(warpDeleter.getUniqueId(), false);
        VerifyInventory verifyInventory = new VerifyInventory(warpDeleter);
        verifyInventory.openInventory(warpDeleter);
    }

    private void deleteWarpIfPossible(Player warpDeleter, Warp warp) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            if (plugin.getDataHolder().getDeleteWarpsVerification() != null && plugin.getDataHolder().getDeleteWarpsVerification().get(warpDeleter.getUniqueId())) {
                plugin.getWarpManager().removeWarp(warp.getName());
                plugin.getDataHandler().getHandleData().removeWarp(warp);
            } else {
                warpDeleter.closeInventory();
            }
        }, 100L);
    }
}
