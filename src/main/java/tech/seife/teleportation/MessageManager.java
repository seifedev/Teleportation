package tech.seife.teleportation;

import org.bukkit.Location;

public class MessageManager {

    public String replacePlayerName(String message, String playerName) {
        return message.replaceAll("%player%", playerName);
    }

    public String replaceHomeName(String message, String homeName) {
        return message.replaceAll("%home%", homeName);
    }

    public String replaceViewHome(String message, String ownerName, String homeName, Location location) {
        message = replaceHomeName(message, homeName);
        message = replaceWarpPoint(message, location, ownerName);
        return replacePlayerName(message, ownerName);
    }

    public String replaceWarpPoint(String message, Location location, String name) {
        message = message.replaceAll("%location%", "the world: " + location.getWorld().getName() + " X: " + location.getX() + " Y: " + location.getY() + " Z: " + location.getZ());
        return message.replaceAll("%teleportPoint%", name);
    }
}
