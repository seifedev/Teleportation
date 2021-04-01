package tech.seife.teleportation.datamanager;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import tech.seife.teleportation.Teleportation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class FileManager {

    private final Teleportation plugin;

    private File homeFile;
    private File warpFile;
    private File invitationsFile;
    private File spawnFile;
    private File portalsFile;
    private File translationFile;
    private File signFile;
    private Gson gson;
    private FileConfiguration translationConfig;

    public FileManager(Teleportation plugin) {
        this.plugin = plugin;
        createTranslationFile(plugin);
    }

    public void createGson() {
        gson = new Gson();

        homeFile = new File(plugin.getDataFolder(), "homes.json");
        createJsonFile(homeFile);

        warpFile = new File(plugin.getDataFolder(), "warps.json");
        createJsonFile(warpFile);

        invitationsFile = new File(plugin.getDataFolder(), "invitations.json");
        createJsonFile(invitationsFile);

        spawnFile = new File(plugin.getDataFolder(), "spawn.json");
        createJsonFile(spawnFile);

        portalsFile = new File(plugin.getDataFolder(), "portals.json");
        createJsonFile(portalsFile);

        signFile = new File(plugin.getDataFolder(), "sign.json");
        createJsonFile(signFile);
    }

    public void createTranslationFile(Teleportation plugin) {
        translationConfig = new YamlConfiguration();

        translationFile = new File(plugin.getDataFolder(), "translation.yml");
        createYamlFile(translationFile);
    }

    public Map getHomeFile() {
        return getGson(homeFile);
    }

    public Map getWarpFile() {
        return getGson(warpFile);
    }

    public Map getInvitationsFile() {
        return getGson(invitationsFile);
    }

    public Map getSpawnFile() {
        return getGson(spawnFile);
    }

    public Map getPortalsFile() {
        return getGson(portalsFile);
    }

    public Map getSignFile() {
        return getGson(signFile);
    }

    public void saveHomeFile(Map map) {
        saveJson(homeFile, map);
    }

    public void saveWarpFile(Map map) {
        saveJson(warpFile, map);
    }

    public void saveInvitationsFile(Map map) {
        saveJson(invitationsFile, map);
    }

    public void saveSpawnFile(Map map) {
        saveJson(spawnFile, map);
    }

    public void savePortalsFile(Map map) {
        saveJson(portalsFile, map);
    }

    public void saveSignFile(Map map) {
        saveJson(signFile, map);
    }

    public void saveJson(File file, Map map) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String jsonString = gson.toJson(map);

            file.delete();

            try {
                Files.write(file.toPath(), jsonString.getBytes());
            } catch (IOException e) {
                plugin.getLogger().log(Level.WARNING, "Failed to save json!\nErrorMessage: " + e.getMessage());
            }
        });
    }

    public Gson getGson() {
        return gson;
    }

    public FileConfiguration getTranslationConfig() {
        return translationConfig;
    }

    private void createYamlFile(File file) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(file.getName(), false);
        }

        translationConfig = new YamlConfiguration();

        try {
            translationConfig.load(translationFile);
        } catch (IOException | InvalidConfigurationException e) {
            plugin.getLogger().log(Level.WARNING, "Couldn't load translation config file!\nError message: ", e.getMessage());
        }
    }

    private void createJsonFile(File file) {
        if (!file.exists()) {
            plugin.saveResource(file.getName(), false);
        }
    }


    private HashMap getGson(File file) {
        try {
            return gson.fromJson(new FileReader(file), HashMap.class);
        } catch (FileNotFoundException e) {
            plugin.getLogger().log(Level.WARNING, file.getName() + " wasn't found");
        }
        return null;
    }

}
