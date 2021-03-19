package tech.seife.teleportation.datamanager;

import tech.seife.teleportation.Teleportation;
import tech.seife.teleportation.homes.Home;
import tech.seife.teleportation.signs.Sign;
import tech.seife.teleportation.warps.Warp;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

public class HandleDataFiles implements HandleData {

    private final Teleportation plugin;

    public HandleDataFiles(Teleportation plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean isHomeValidUuid(UUID owner, String homeName) {
        return false;
    }

    @Override
    public Home getHomeUuid(UUID owner, String homeName) {
        return null;
    }

    @Override
    public void saveHome(Home home) {

    }

    @Override
    public void removeHome(Home home) {

    }

    @Override
    public int getLatestIdOfHomes() {
        return -1;
    }

    @Override
    public List<String> getHomeNamesOfPlayer(UUID playerUuid) {
        return null;
    }

    @Override
    public int getNumberOfHomes(UUID playerUuid) {
        return -1;
    }

    @Override
    public boolean isValidInvitation(UUID invitedUuid, UUID invitedBy, String homeName) {
        return false;
    }


    @Override
    public boolean isHomeValidUsername(String owner, String homeName) {
        return false;
    }

    @Override
    public void saveInvitation(String invited, String inviter, String homeName) {

    }

    @Override
    public void removeInvitation(UUID invited, UUID inviter, String homeName) {

    }

    @Override
    public void saveWarp(Warp warp) {

    }

    @Override
    public void loadWarps() {

    }

    @Override
    public void removeWarp(Warp warp) {

    }

    @Override
    public void saveSpawnLocation(Location location) {

    }

    @Override
    public void loadSpawnLocation() {

    }

    @Override
    public void loadSigns() {

    }

    @Override
    public void saveSign(Sign sign) {

    }

    @Override
    public void removeSign(Sign sign) {

    }

    @Override
    public int getLatestIdOfSigns() {
        return 0;
    }

    @Override
    public void savePortals(String name, Location portalLocation, Location linkedLocation) {

    }

    @Override
    public void removePortal(String portalName) {

    }

    @Override
    public void loadPortals() {

    }
}
