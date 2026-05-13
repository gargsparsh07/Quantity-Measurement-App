package com.app.quantitymeasurement.util;

import com.app.quantitymeasurement.exception.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ConnectionPool {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);
    private static ConnectionPool instance;

    private final BlockingQueue<Connection> pool;
    private final List<Connection> allConnections;
    private final ApplicationConfig config;
    private final int poolSize;

    private ConnectionPool() {
        this.config = ApplicationConfig.getInstance();
        this.poolSize = config.getPoolSize();
        this.pool = new ArrayBlockingQueue<>(poolSize);
        this.allConnections = new ArrayList<>();
        initializePool();
    }

    public static synchronized ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    private void initializePool() {
        try {
            Class.forName(config.getDbDriver());
            for (int i = 0; i < poolSize; i++) {
                Connection conn = createConnection();
                pool.offer(conn);
                allConnections.add(conn);
            }
            logger.info("Connection pool initialized with {} connections", poolSize);
        } catch (ClassNotFoundException | SQLException e) {
            throw new DatabaseException("Failed to initialize connection pool: " + e.getMessage(), e);
        }
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(
            config.getDbUrl(),
            config.getDbUsername(),
            config.getDbPassword()
        );
    }

    public Connection acquireConnection() {
        try {
            Connection conn = pool.poll(config.getPoolTimeout(), TimeUnit.MILLISECONDS);
            if (conn == null) {
                throw new DatabaseException("Timeout: no connection available in pool");
            }
            if (conn.isClosed()) {
                conn = createConnection();
                allConnections.add(conn);
            }
            logger.debug("Connection acquired. Available: {}", pool.size());
            return conn;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DatabaseException("Interrupted while waiting for connection", e);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to acquire connection: " + e.getMessage(), e);
        }
    }

    public void releaseConnection(Connection conn) {
        if (conn != null) {
            pool.offer(conn);
            logger.debug("Connection released. Available: {}", pool.size());
        }
    }

    public String getPoolStatistics() {
        return String.format("Pool[total=%d, available=%d, inUse=%d]",
            allConnections.size(), pool.size(), allConnections.size() - pool.size());
    }

    public void closeAll() {
        for (Connection conn : allConnections) {
            try {
                if (!conn.isClosed()) conn.close();
            } catch (SQLException e) {
                logger.error("Error closing connection: {}", e.getMessage());
            }
        }
        pool.clear();
        allConnections.clear();
        instance = null;
        logger.info("All connections closed");
    }
}
