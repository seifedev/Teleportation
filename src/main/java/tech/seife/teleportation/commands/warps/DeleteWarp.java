package tech.seife.teleportation.commands.warps;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.seife.teleportation.MessageManager;
import tech.seife.teleportation.Teleportation;
import tech.seife.teleportation.enums.ReplaceType;
import tech.seife.teleportation.inventories.VerifyInventory;
import tech.seife.teleportation.warps.Warp;

import java.util.HashMap;
import java.util.Map;

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

                Map<ReplaceType, String> values = new HashMap<>();

                values.put(ReplaceType.WARP_NAME, warp.getName());

                sender.sendMessage(MessageManager.getTranslatedMessageWithReplace(plugin, "warpDeleted", values));
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
