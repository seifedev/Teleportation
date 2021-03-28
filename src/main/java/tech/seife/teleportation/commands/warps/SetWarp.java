package tech.seife.teleportation.commands.warps;

import tech.seife.teleportation.MessageManager;
import tech.seife.teleportation.Teleportation;
import tech.seife.teleportation.enums.ReplaceType;
import tech.seife.teleportation.warps.Warp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SetWarp implements CommandExecutor {

    private final Teleportation plugin;

    public SetWarp(Teleportation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 5 && args[0] != null && args[1] != null && args[2] != null && args[3] != null && args[4] != null) {
            World world = Bukkit.getWorld(args[1]);
            if (world == null) return false;

            double x = parseDouble(args[2]);
            double y = parseDouble(args[3]);
            double z = parseDouble(args[4]);

            Location location = new Location(world, x, y, z);

            return createWarp(sender, args[0], location);
        } else if (sender instanceof Player && args.length == 1 && args[0] != null) {
            return createWarp(sender, args[0], ((Player) sender).getLocation());
        }
        return true;
    }

    private long getParsedLong(String numberToParse) {
        try {
            return Long.parseLong(numberToParse);
        } catch (NumberFormatException e) {
            Bukkit.getLogger().warning("Couldn't parse: " + numberToParse);
        }
        return -1;

    }

    private double parseDouble(String numberToParse) {
        try {
            return Double.parseDouble(numberToParse);
        } catch (NumberFormatException e) {
            Bukkit.getLogger().warning("Couldn't parse: " + numberToParse);
        }
        return -1;
    }

    private void sendMessage(CommandSender sender, Location location, String name) {
        Map<ReplaceType, String> values = new HashMap<>();

        values.put(ReplaceType.WARP_NAME, name);

        sender.sendMessage(MessageManager.getTranslatedMessageWithReplace(plugin, "newWarp", values));
    }

    private boolean createWarp(CommandSender sender, String name, Location location) {
        if (plugin.getWarpManager().getWarp(name) == null) {
            plugin.getWarpManager().addWarp(name, location);

            Warp warp = plugin.getWarpManager().getWarp(name);
            plugin.getDataHandler().getHandleData().saveWarp(warp);

            sendMessage(sender, ((Player) sender).getLocation(), name);
            return true;
        }
        return false;
    }
}
