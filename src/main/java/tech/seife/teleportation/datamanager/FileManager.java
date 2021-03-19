package tech.seife.teleportation.datamanager;

import tech.seife.teleportation.Teleportation;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileManager {

    private final Teleportation plugin;

    private final File homeFile, warpFile, invitationsFile, spawnFile, portalsFile;
    private final FileConfiguration homeConfig, warpsConfig, invitationsConfig, spawnConfig, portalsConfig;

    public FileManager(Teleportation plugin) {
        this.plugin = plugin;

        homeFile = new File(plugin.getDataFolder(), "homes.yml");
        homeConfig = new YamlConfiguration();
        createFile(homeConfig, homeFile, "homes.yml");

        warpFile = new File(plugin.getDataFolder(), "warps.yml");
        warpsConfig = new YamlConfiguration();
        createFile(warpsConfig, warpFile, "warps.yml");

        invitationsFile = new File(plugin.getDataFolder(), "invitations.yml");
        invitationsConfig = new YamlConfiguration();
        createFile(invitationsConfig, invitationsFile, "invitations.yml");

        spawnFile = new File(plugin.getDataFolder(), "spawns.yml");
        spawnConfig = new YamlConfiguration();
        createFile(spawnConfig, spawnFile, "spawns.yml");

        portalsFile = new File(plugin.getDataFolder(), "portals.yml");
        portalsConfig = new YamlConfiguration();
        createFile(portalsConfig, portalsFile, "portals.yml");

    }

    private void createFile(FileConfiguration configFile, File file, String configName) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(configName, false);
        }
        try {
            configFile.load(file);
        } catch (InvalidConfigurationException | IOException e) {
            Bukkit.getLogger().warning(e.getMessage());
        }
    }

    public void savePortalsConfig() {
        saveFile(portalsConfig, portalsFile);
    }

    public void saveSpawnConfig() {
        saveFile(spawnConfig, spawnFile);
    }

    public void saveHomeConfig() {
        saveFile(homeConfig, homeFile);
    }

    public void saveWarpsFile() {
        saveFile(warpsConfig, warpFile);
    }

    public void saveInvitationFile() {
        saveFile(invitationsConfig, invitationsFile);
    }

    private void saveFile(FileConfiguration fileConfig, File file) {
        try {
            fileConfig.save(file);
        } catch (IOException e) {
            Bukkit.getLogger().warning("Error message: " + e.getMessage());
        }
    }


    public FileConfiguration getInvitationsConfig() {
        return invitationsConfig;
    }

    public FileConfiguration getHomeConfig() {
        return homeConfig;
    }

    public FileConfiguration getWarpsConfig() {
        return warpsConfig;
    }

    public FileConfiguration getSpawnConfig() {
        return spawnConfig;
    }

    public FileConfiguration getPortalsConfig() {
        return portalsConfig;
    }
}
