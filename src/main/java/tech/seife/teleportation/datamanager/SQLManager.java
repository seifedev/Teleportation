package tech.seife.teleportation.datamanager;

import tech.seife.teleportation.Teleportation;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLManager {

    private final Teleportation plugin;
    private final ConnectionPoolManager connectionPoolManager;

    public SQLManager(Teleportation plugin) {
        this.plugin = plugin;
        connectionPoolManager = new ConnectionPoolManager(plugin.getConfig());
        createTables();
    }

    private void createTables() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            createLocationTable();
            createPlayersHome();
            createHomeTable();
            createWarpTable();
            createInvitationTable();
            createSpawnTable();
            createSignsTable();
            createPortalsTable();
        });
    }

    private void createHomeTable() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS homes" +
                "(id INTEGER NOT NULL AUTO_INCREMENT," +
                "ownerUuid VARCHAR (36) NOT NULL," +
                "ownerName VARCHAR(500) NOT NULL," +
                "homeName INTEGER NOT NULL," +
                "FOREIGN KEY(home) REFERENCES players_home (id) ON UPDATE CASCADE ON DELETE CASCADE," +
                "PRIMARY KEY (id));";
        runQuery(sqlQuery);
    }

    private void createPlayersHome() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS players_home" +
                "(id INTEGER NOT NULL AUTO_INCREMENT," +
                "name VARCHAR(255) NOT NULL," +
                "location INTEGER NOT NULL," +
                "FOREIGN KEY (location) REFERENCES locations (id) ON UPDATE CASCADE ON DELETE CASCADE," +
                "PRIMARY KEY(id));";

        runQuery(sqlQuery);
    }


    private void createWarpTable() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS warps" +
                "(id INTEGER NOT NULL AUTO_INCREMENT," +
                "location INTEGER NOT NULL," +
                "name varchar(255) NOT NULL," +
                "FOREIGN KEY (location) REFERENCES locations (id) ON UPDATE CASCADE ON DELETE CASCADE," +
                "PRIMARY KEY (id));";

        runQuery(sqlQuery);
    }

    private void createLocationTable() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS locations" +
                "(id INTEGER NOT NULL AUTO_INCREMENT," +
                "world varchar(255) NOT NULL, " +
                "x DOUBLE NOT NULL," +
                "y DOUBLE NOT NULL," +
                "z DOUBLE NOT NULL," +
                "PRIMARY KEY (id));";

        runQuery(sqlQuery);
    }

    private void createInvitationTable() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS invitations" +
                "(id INTEGER NOT NULL AUTO_INCREMENT," +
                "invited varchar(36) NOT NULL," +
                "homes INTEGER NOT NULL," +
                "FOREIGN KEY (homes) REFERENCES players_home (id) ON UPDATE CASCADE ON DELETE CASCADE," +
                "PRIMARY KEY (id));";

        runQuery(sqlQuery);
    }

    private void createSpawnTable() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS spawn" +
                "(id INTEGER NOT NULL AUTO_INCREMENT," +
                "location INTEGER NOT NULL," +
                "FOREIGN KEY (location) REFERENCES locations (id) ON UPDATE CASCADE ON DELETE CASCADE," +
                "PRIMARY KEY (id));";

        runQuery(sqlQuery);
    }

    private void createSignsTable() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS signs" +
                "(id INTEGER NOT NULL AUTO_INCREMENT," +
                "sign_location INTEGER NOT NULL," +
                "location_to_teleport INTEGER NOT NULL," +
                "FOREIGN KEY (sign_location) REFERENCES locations (id) ON UPDATE CASCADE ON DELETE CASCADE," +
                "FOREIGN KEY (location_to_teleport) REFERENCES locations (id) ON UPDATE CASCADE ON DELETE CASCADE," +
                "PRIMARY KEY (id));";

        runQuery(sqlQuery);
    }

    private void createPortalsTable() {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS portals" +
                "(id INTEGER NOT NULL AUTO_INCREMENT," +
                "name VARCHAR(200) NOT NULL," +
                "portal_location INTEGER NOT NULL," +
                "location_to_teleport INTEGER NOT NULL," +
                "FOREIGN KEY (portal_location) REFERENCES locations (id) ON UPDATE CASCADE ON DELETE CASCADE," +
                "FOREIGN KEY (location_to_teleport) REFERENCES locations (id) ON UPDATE CASCADE ON DELETE CASCADE," +
                "PRIMARY KEY (id));";

        runQuery(sqlQuery);
    }

    private void runQuery(String sqlQuery) {
        try (Connection connection = connectionPoolManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlQuery)) {
            ps.execute();
        } catch (SQLException e) {
            Bukkit.getLogger().warning("Error while creating table\nError message: " + e.getMessage());
        }
    }
}
