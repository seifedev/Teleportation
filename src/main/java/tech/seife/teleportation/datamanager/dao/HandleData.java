package tech.seife.teleportation.datamanager.dao;

import tech.seife.teleportation.homes.Home;
import tech.seife.teleportation.signs.Sign;
import tech.seife.teleportation.warps.Warp;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

public interface HandleData {

    boolean isHomeValidUuid(UUID owner, String homeName);

    boolean isHomeValidUsername(String owner, String homeName);

    Home    getHomeUuid(UUID owner, String homeName);

    void saveHome(Home home);

    void removeHome(Home home);

    int getLatestIdOfHomes();

    int getNumberOfHomes(UUID playerUuid);

    List<String> getHomeNamesOfPlayer(UUID playerUuid);

    boolean isValidInvitation(UUID invitedUuid, UUID invitedBy, String homeName);

    void saveInvitation(String invited, String inviter, String homeName);

    void removeInvitation(UUID invited, UUID inviter, String homeName);

    void saveWarp(Warp warp);

    void loadWarps();

    void removeWarp(Warp warp);

    void saveSpawnLocation(Location location);

    void loadSpawnLocation();

    void loadSigns();

    void saveSign(Sign sign);

    void removeSign(Sign sign);

    int getLatestIdOfSigns();

    void savePortals(String name, Location portalLocation, Location linkedLocation);

    void removePortal(String portalName);

    boolean doesPortalExist(String portalName);

    void loadPortals();

}
