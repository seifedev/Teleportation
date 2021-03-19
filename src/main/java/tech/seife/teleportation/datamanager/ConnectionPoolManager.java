package tech.seife.teleportation.datamanager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPoolManager {

    private final Configuration config;

    private HikariDataSource dataSource;

    private String hostname;
    private String port;
    private String database;
    private String username;
    private String password;

    private int minimumConnections;
    private int maximumConnections;
    private int connectionsTimeout;
    private int idleTimeout;

    private boolean secureConnection;

    public ConnectionPoolManager(FileConfiguration config) {
        this.config = config;
        initialize();
        poolSetup();
    }

    private void initialize() {
        hostname = config.getString("hostname");
        port = config.getString("port");
        database = config.getString("database");
        username = config.getString("username");
        password = config.getString("password");

        secureConnection = config.getBoolean("useSecureConnection");

        minimumConnections = config.getInt("minimumConnections");
        maximumConnections = config.getInt("maximumConnections");
        connectionsTimeout = config.getInt("connectionsTimeout");
        idleTimeout = config.getInt("idleTimeout");
    }

    private void poolSetup() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://" + hostname + ":" + port + "/" + database);

        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(maximumConnections);
        config.setMinimumIdle(minimumConnections);
        config.setConnectionTimeout(connectionsTimeout);
        config.setIdleTimeout(idleTimeout);

        config.addDataSourceProperty("useSSL", secureConnection);
        config.addDataSourceProperty("requireSSL", secureConnection);
        config.addDataSourceProperty("verifyServerCertificate", secureConnection);
        config.addDataSourceProperty("foreign_keys", "true");
        dataSource = new HikariDataSource(config);
    }

    public Connect ion getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
