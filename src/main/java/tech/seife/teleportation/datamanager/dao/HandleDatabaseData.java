package tech.seife.teleportation.datamanager.dao;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import tech.seife.teleportation.Teleportation;
import tech.seife.teleportation.homes.Home;
import tech.seife.teleportation.signs.Sign;
import tech.seife.teleportation.warps.Warp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class HandleDatabaseData implements HandleData {

    private final Teleportation plugin;

    public HandleDatabaseData(Teleportation plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isHomeValidUuid(UUID owner, String homeName) {
        String sqlQuery = "SELECT * FROM players_home INNER JOIN  homes WHERE homes.ownerUuid = ? and players_home.name = ?;";

        try (Connection connection = plugin.getConnectionPoolManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ps.setString(1, owner.toString());
            ps.setString(2, homeName);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to verify home owner!\nError message: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean isHomeValidUsername(String owner, String homeName) {
        String sqlQuery = "SELECT * FROM players_home INNER JOIN  homes WHERE ownerName = ? and name = ?;";

        try (Connection connection = plugin.getConnectionPoolManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ps.setString(1, owner);
            ps.setString(2, homeName);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to verify home owner!\nError message: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Home getHomeUuid(UUID owner, String homeName) {
        String sqlQuery = "SELECT * FROM players_home INNER JOIN homes WHERE homes.ownerUuid = ? and players_home.name = ?;";
        try (Connection connection = plugin.getConnectionPoolManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ps.setString(1, owner.toString());
            ps.setString(2, homeName);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Home(rs.getInt("homes.id"), UUID.fromString(rs.getString("homes.ownerUuid")), rs.getString("homes.ownerName"), rs.getString("players_home.name"), getLocationFromId(rs.getInt("players_home.location")));
            }

        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load home !\nError message: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void saveHome(Home home) {

        String sqlQuery = "INSERT INTO homes (ownerUuid, ownerName, home) VALUES (?, ?, ?);";

        try (Connection connection = plugin.getConnectionPoolManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery, RETURN_GENERATED_KEYS)) {

            connection.setAutoCommit(false);

            int locationId = saveLocationAndGetId(home.getLocation(), connection);
            int playersHomeId = saveHomeAndGetId(home.getHomeName(), locationId, connection);


            ps.setString(1, home.getOwnerUuid().toString());
            ps.setString(2, home.getOwnerName());
            ps.setInt(3, playersHomeId);

            connection.setAutoCommit(true);

            ps.executeUpdate();

        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to save home!\nError message: " + e.getMessage());
        }
    }

    private int saveHomeAndGetId(String homeName, int locationId, Connection connection) {
        String sqlQuery = "INSERT INTO players_home(name, location) VALUES(?, ?);";

        try (PreparedStatement ps = connection.prepareStatement(sqlQuery, RETURN_GENERATED_KEYS)) {

            ps.setString(1, homeName);
            ps.setInt(2, locationId);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;

        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to save player's home id !\nError message: " + e.getMessage());
        }
        return -1;

    }

    private int saveLocationAndGetId(Location location, Connection connection) {
        String sqlQuery = "INSERT INTO locations(world, x, y, z) VALUES(?, ?, ?, ?);";

        try (PreparedStatement ps = connection.prepareStatement(sqlQuery, RETURN_GENERATED_KEYS)) {

            ps.setString(1, location.getWorld().getName());
            ps.setDouble(2, location.getX());
            ps.setDouble(3, location.getY());
            ps.setDouble(4, location.getZ());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;

        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to save location's id !\nError message: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public void removeHome(Home home) {
        String sqlQuery = "DELETE homes, ph, l FROM homes LEFT JOIN players_home ph on ph.id = homes.id LEFT JOIN locations l on ph.location = l.id WHERE homes.id = ?";

        try (Connection connection = plugin.getConnectionPoolManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ps.setInt(1, home.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to delete home!\nError message: " + e.getMessage());
        }
    }

    @Override
    public int getLatestIdOfHomes() {

        String sqlQuery = "SELECT id FROM homes ORDER BY id DESC LIMIT 1";

        try (Connection connection = plugin.getConnectionPoolManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ResultSet rs = ps.executeQuery();

            return rs.next() ? rs.getInt(1) : -1;

        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to retrieve latest home id!\nError message: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public List<String> getHomeNamesOfPlayer(UUID playerUuid) {
        String sqlQuery = "SELECT * from homes INNER JOIN players_home WHERE ownerUuid = ?;";

        try (Connection connection = plugin.getConnectionPoolManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ps.setString(1, playerUuid.toString());

            List<String> homeNames = new ArrayList<>();

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                homeNames.add(rs.getString("name"));
            }

            return homeNames;

        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to retrieve home names!\nError message: " + e.getMessage());
        }

        return null;
    }

    @Override
    public int getNumberOfHomes(UUID playerUuid) {
        String sqlQuery = "SELECT COUNT(*) AS total FROM homes INNER JOIN players_home WHERE ownerUuid = ?;";

        try (Connection connection = plugin.getConnectionPoolManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ps.setString(1, playerUuid.toString());

            ResultSet rs = ps.executeQuery();

            return rs.next() ? rs.getInt("total") : -1;

        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to retrieve number of homes!\nError message: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public boolean isValidInvitation(UUID invitedUuid, UUID invitedBy, String homeName) {
        String sqlQuery = "SELECT COUNT(*) AS total FROM invitations INNER join players_home INNER join homes WHERE invited = ? and ownerUuid = ? and name = ?;";


        try (Connection connection = plugin.getConnectionPoolManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ps.setString(1, invitedUuid.toString());
            ps.setString(2, invitedBy.toString());
            ps.setString(3, homeName);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("total") >= 1;
            }

        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to verify if the invitation is valid!\nError message: " + e.getMessage());
        }

        return false;
    }

    @Override
    public void saveInvitation(String inviter, String invited, String homeName) {
        String sqlQuery = "INSERT INTO invitations (invited, homes) VALUES (?, ?);";

        try (Connection connection = plugin.getConnectionPoolManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            connection.setAutoCommit(false);

            int homeId = getHomeId(inviter, homeName, connection);

            if (homeId != -1) {
                ps.setString(1, invited);
                ps.setInt(2, homeId);

                connection.setAutoCommit(true);

                ps.executeUpdate();
            } else {
                plugin.getLogger().log(Level.WARNING, "failed to save invitation");
            }

        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed too save the invitation!\nError message: " + e.getMessage());
        }
    }

    private int getHomeId(String inviter, String homeName, Connection connection) {
        String sqlQuery = "SELECT * FROM homes INNER JOIN players_home WHERE name = ? and ownerUuid = ?;";

        try (PreparedStatement ps = connection.prepareStatement(sqlQuery, RETURN_GENERATED_KEYS)) {

            ps.setString(1, homeName);
            ps.setString(2, inviter);

            ResultSet rs = ps.executeQuery();

            return rs.next() ? rs.getInt("id") : -1;


        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to get homeId!\nError message: " + e.getMessage());
        }

        return -1;
    }

    @Override
    public void removeInvitation(UUID invited, UUID inviter, String homeName) {
        String sqlQuery = "DELETE invitations, ph, l FROM invitations LEFT JOIN players_home ph on ph.id = invitations.homes LEFT JOIN locations l on ph.location = l.id WHERE invitations.id = ?";

        try (Connection connection = plugin.getConnectionPoolManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ps.setString(1, invited.toString());
            ps.setString(2, inviter.toString());
            ps.setString(3, homeName);

            ps.executeUpdate();

        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to delete invitation!\nError message: " + e.getMessage());
        }

    }

    @Override
    public void saveWarp(Warp warp) {

        String sqlQuery = "INSERT INTO warps (location, name) VALUES (?, ?);";

        try (Connection connection = plugin.getConnectionPoolManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            connection.setAutoCommit(false);

            int locationId = saveLocationAndGetId(warp.getLocation(), connection);

            ps.setInt(1, locationId);
            ps.setString(2, warp.getName());

            connection.setAutoCommit(true);

            ps.executeUpdate();

        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to save warp!\nError message: " + e.getMessage());
        }
    }

    @Override
    public void loadWarps() {
        String sqlQuery = "SELECT * FROM warps INNER JOIN locations WHERE location = locations.id;";

        try (Connection connection = plugin.getConnectionPoolManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                plugin.getWarpManager().addWarp(new Warp(rs.getInt("warps.id"), rs.getString("name"), getLocationFromResultSet(rs)));
            }

        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load warps!\nError message: " + e.getMessage());
        }
    }

    @Override
    public void removeWarp(Warp warp) {
        String sqlQuery = "DELETE warps FROM warps LEFT JOIN locations l on warps.location = l.id WHERE warps.id = ?;";

        try (Connection connection = plugin.getConnectionPoolManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ps.setInt(1, warp.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to delete warp!\nError message: " + e.getMessage());
        }
    }

    @Override
    public void saveSpawnLocation(Location location) {

        String sqlQuery = "INSERT INTO spawn (location) VALUE (?);";

        try (Connection connection = plugin.getConnectionPoolManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            connection.setAutoCommit(false);

            ps.setInt(1, saveLocationAndGetId(location, connection));

            connection.setAutoCommit(true);

            ps.executeUpdate();

        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to save spawn location!\nError message: " + e.getMessage());
        }
    }

    @Override
    public void loadSpawnLocation() {
        String sqlQuery = "SELECT * FROM spawn";

        try (Connection connection = plugin.getConnectionPoolManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                plugin.getDataHolder().setSpawnLocation(getLocationFromId(rs.getInt("location")));
            }

        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load spawn location!\nError message: " + e.getMessage());
        }
    }

    @Override
    public void loadSigns() {
        String sqlQuery = "SELECT * FROM signs;";

        try (Connection connection = plugin.getConnectionPoolManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                plugin.getSignManager().addSign(rs.getInt("id"), getLocationFromId(rs.getInt("sign_location")), getLocationFromId(rs.getInt("location_to_teleport")));
            }

        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Error while loading signs!\nError message: " + e.getMessage());
        }
    }

    @Override
    public void saveSign(Sign sign) {
        String sqlQuery = "INSERT INTO signs (sign_location, location_to_teleport) VALUES (?, ?);";

        try (Connection connection = plugin.getConnectionPoolManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            connection.setAutoCommit(false);

            int signLocationId = saveLocationAndGetId(sign.getSignLocation(), connection);
            int locationToTeleportId = saveLocationAndGetId(sign.getLocationToTeleport(), connection);

            ps.setInt(1, signLocationId);
            ps.setInt(2, locationToTeleportId);

            connection.setAutoCommit(true);

            ps.executeUpdate();

        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Error while saving sign!\nError message: " + e.getMessage());
        }
    }

    @Override
    public void removeSign(Sign sign) {

        String sqlQuery = "DELETE signs FROM signs LEFT JOIN locations l on l.id = signs.location_to_teleport LEFT JOIN locations l2 on l2.id = signs.sign_location WHERE l.id = ?";

        try (Connection connection = plugin.getConnectionPoolManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ps.setInt(1, sign.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Error while deleting sign!\nError message: " + e.getMessage());
        }
    }

    @Override
    public int getLatestIdOfSigns() {

        String sqlQuery = "SELECT id FROM signs ORDER BY id DESC LIMIT 1";

        try (Connection connection = plugin.getConnectionPoolManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ResultSet rs = ps.executeQuery();

            return rs.next() ? rs.getInt(1) : -1;

        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to retrieve latest home id!\nError message: " + e.getMessage());
        }
        return -1;

    }

    @Override
    public void savePortals(String name, Location portalLocation, Location linkedLocation) {

        String sqlQuery = "INSERT INTO portals (name, portal_location, location_to_teleport) VALUES (?, ?, ?);";

        try (Connection connection = plugin.getConnectionPoolManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            connection.setAutoCommit(false);

            int portalLocationId = saveLocationAndGetId(portalLocation, connection);
            int linkedLocationId = saveLocationAndGetId(linkedLocation, connection);

            ps.setString(1, name);
            ps.setInt(2, portalLocationId);
            ps.setInt(3, linkedLocationId);

            connection.setAutoCommit(true);

            ps.executeUpdate();

        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to save portal!\nError message: " + e.getMessage());
        }
    }

    @Override
    public void removePortal(String portalName) {

        String sqlQuery = "DELETE portals FROM portals LEFT JOIN locations l on l.id = portals.location_to_teleport LEFT JOIN locations l2 on l2.id = portals.portal_location WHERE portals.name = ?;";

        try (Connection connection = plugin.getConnectionPoolManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ps.setString(1, portalName);

            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to delete portal!\nError message: " + e.getMessage());
        }

    }

    @Override
    public void loadPortals() {
        String sqlQuery = "SELECT * FROM portals;";

        try (Connection connection = plugin.getConnectionPoolManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                plugin.getDataHolder().getPortalLocationWarp().put(getLocationFromId(rs.getInt("portal_location")), getLocationFromId(rs.getInt("location_to_teleport")));
            }

        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load portals!\nError message: " + e.getMessage());
        }
    }

    private Location getLocationFromId(int id) {

        String sqlQuery = "SELECT * FROM locations WHERE id = ?;";

        try (Connection connection = plugin.getConnectionPoolManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return getLocationFromResultSet(rs);
            }

        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load location from id!\nError message: " + e.getMessage());
        }

        return null;
    }

    @Override
    public boolean doesPortalExist(String portalName) {
        String sqlQuery = "SELECT * FROM portals LEFT JOIN locations l on l.id = portals.location_to_teleport LEFT JOIN locations l2 on l2.id = portals.portal_location WHERE portals.name = ?;";

        try (Connection connection = plugin.getConnectionPoolManager().getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {

            ps.setString(1, portalName);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }

        } catch (SQLException e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load location from id!\nError message: " + e.getMessage());
        }
        return false;
    }

    private Location getLocationFromResultSet(ResultSet rs) throws SQLException {
        return new Location(Bukkit.getWorld(rs.getString("world")), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"));
    }

}
