package tech.seife.teleportation.datamanager;

import tech.seife.teleportation.Teleportation;
import org.bukkit.Bukkit;

public class DataHandler {

    private final Teleportation plugin;
    private final HandleData handleData;

    public DataHandler(Teleportation plugin) {
        this.plugin = plugin;

        if (plugin.getConfig().getBoolean("useDatabase")) {
            handleData = new HandleDatabaseData(plugin);
        } else {
            handleData = new HandleDataFiles(plugin);
        }
        loadData();
    }

    private void loadData() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
           handleData.loadWarps();
           handleData.loadSpawnLocation();
           handleData.loadPortals();
           handleData.loadSigns();
        });
    }

    public HandleData getHandleData() {
        return handleData;
    }
}
