package tech.seife.teleportation;

import org.bukkit.ChatColor;
import tech.seife.teleportation.enums.ReplaceType;

import java.util.Map;

public class MessageManager {

    public static String getTranslatedMessageWithReplace(Teleportation plugin, String path, Map<ReplaceType, String> values) {
        if (plugin.getFileManager() != null && plugin.getFileManager().getTranslationConfig() != null && plugin.getFileManager().getTranslationConfig().getString(path) != null) {
            String message = plugin.getFileManager().getTranslationConfig().getString(path);

            for (Map.Entry<ReplaceType, String> entry : values.entrySet()) {
                message = message.replaceAll(entry.getKey().getValue(), entry.getValue());
            }
            return ChatColor.translateAlternateColorCodes('&', message);
        }
        return "none";
    }

    public static String getTranslatedMessage(Teleportation plugin, String path) {
        if (plugin.getFileManager() != null && plugin.getFileManager().getTranslationConfig() != null && plugin.getFileManager().getTranslationConfig().getString(path) != null) {
            return  ChatColor.translateAlternateColorCodes('&', plugin.getFileManager().getTranslationConfig().getString(path));
        }
        return null;
    }
}
