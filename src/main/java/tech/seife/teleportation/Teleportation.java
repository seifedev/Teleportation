package tech.seife.teleportation;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import tech.seife.teleportation.commands.homes.*;
import tech.seife.teleportation.commands.requeststeleports.RequestTeleportHere;
import tech.seife.teleportation.commands.requeststeleports.RequestTeleportThere;
import tech.seife.teleportation.commands.teleports.*;
import tech.seife.teleportation.commands.warps.*;
import tech.seife.teleportation.datamanager.*;
import tech.seife.teleportation.datamanager.dao.DataHandler;
import tech.seife.teleportation.events.*;
import tech.seife.teleportation.requestteleport.RequesterTeleportManager;
import tech.seife.teleportation.signs.SignManager;
import tech.seife.teleportation.warps.WarpManager;

import java.util.logging.Level;

public final class Teleportation extends JavaPlugin {

    private DataHolder dataHolder;
    private FileManager fileManager;
    private WarpManager warpManager;
    private RequesterTeleportManager requesterTeleportManager;
    private SignManager signManager;
    private DataHandler dataHandler;
    private MessageManager messageManager;
    private ConnectionPoolManager connectionPoolManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        initialize();
        registerCommands();
        registerEvents();
    }

    private void initialize() {
        if (!getConfig().getBoolean("useDatabase")) {
            getLogger().log(Level.SEVERE, "Initializing files");
            fileManager = new FileManager(this);
        } else {
            getLogger().log(Level.SEVERE, "Initializing database");
            connectionPoolManager = new ConnectionPoolManager(this.getConfig());
            new SQLManager(this);
        }

        warpManager = new WarpManager();
        requesterTeleportManager = new RequesterTeleportManager(this, getConfig());
        requesterTeleportManager.intervalClearingRequests();
        signManager = new SignManager(warpManager);

        messageManager = new MessageManager();
        dataHolder = new DataHolder(this);
        dataHandler = new DataHandler(this);

    }


    private void registerCommands() {
        getCommand("tpa").setExecutor(new RequestTeleportThere(this, requesterTeleportManager));
        getCommand("tpaHere").setExecutor(new RequestTeleportHere(this, requesterTeleportManager));
        getCommand("setHome").setExecutor(new SetHome(this));
        getCommand("Home").setExecutor(new GoHome(this));
        getCommand("deleteHome").setExecutor(new DeleteHome(this));
        getCommand("viewHomes").setExecutor(new ViewHomes(this));
        getCommand("homes").setExecutor(new Homes(this));
        getCommand("setWarp").setExecutor(new SetWarp(this));
        getCommand("warp").setExecutor(new WarpTeleport(this));
        getCommand("deleteWarp").setExecutor(new DeleteWarp(this));
        getCommand("viewWarps").setExecutor(new ViewWarps(this));
        getCommand("rtp").setExecutor(new RandomTeleport(this));
        getCommand("back").setExecutor(new ReturnBack(this));
        getCommand("teleport").setExecutor(new Teleport());
        getCommand("homeInvite").setExecutor(new HomeInvite(this));
        getCommand("visitHome").setExecutor(new VisitHome(this));
        getCommand("revokeInvitation").setExecutor(new RevokeInvitation(this));
        getCommand("BlockSelector").setExecutor(new BlockSelector(this));
        getCommand("setSpawn").setExecutor(new SetSpawn(this));
        getCommand("spawn").setExecutor(new Spawn(this));
        getCommand("LinkTeleportToBlock").setExecutor(new LinkTeleportToBlock(this));
        getCommand("removePortal").setExecutor(new RemovePortal(this));
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new OnInventoryClickEvent(dataHolder), this);
        Bukkit.getPluginManager().registerEvents(new OnPlayerInteractEvent(this), this);
        Bukkit.getPluginManager().registerEvents(new OnPlayerMoveEvent(this), this);
        Bukkit.getPluginManager().registerEvents(new OnPlayerDeathEvent(dataHolder), this);
        Bukkit.getPluginManager().registerEvents(new OnSignChangeEvent(this), this);
    }

    @Override
    public void onDisable() {
        dataHolder.getTasks().cancel();
        if (requesterTeleportManager.getBukkitScheduler() != null) {
            requesterTeleportManager.getBukkitScheduler().cancel();
        }
    }

    public DataHolder getDataHolder() {
        return dataHolder;
    }

    public WarpManager getWarpManager() {
        return warpManager;
    }

    public ConnectionPoolManager getConnectionPoolManager() {
        return connectionPoolManager;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }

    public SignManager getSignManager() {
        return signManager;
    }
}
