package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.exception.DatabaseException;
import com.app.quantitymeasurement.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuantityMeasurementDatabaseRepository implements IQuantityMeasurementRepository {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementDatabaseRepository.class);

    private static final String INSERT_SQL =
        "INSERT INTO quantity_measurement_entity " +
        "(first_quantity, first_unit, second_quantity, second_unit, result, operation_type, measurement_type) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_SQL =
        "SELECT * FROM quantity_measurement_entity ORDER BY created_at DESC";

    private static final String SELECT_BY_OPERATION_SQL =
        "SELECT * FROM quantity_measurement_entity WHERE operation_type = ? ORDER BY created_at DESC";

    private static final String SELECT_BY_TYPE_SQL =
        "SELECT * FROM quantity_measurement_entity WHERE measurement_type = ? ORDER BY created_at DESC";

    private static final String COUNT_SQL =
        "SELECT COUNT(*) FROM quantity_measurement_entity";

    private static final String DELETE_ALL_SQL =
        "DELETE FROM quantity_measurement_entity";

    private final ConnectionPool connectionPool;

    public QuantityMeasurementDatabaseRepository() {
        this.connectionPool = ConnectionPool.getInstance();
        initializeSchema();
        logger.info("QuantityMeasurementDatabaseRepository initialized");
    }

    private void initializeSchema() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("db/schema.sql")) {
            if (is == null) {
                logger.warn("schema.sql not found, skipping schema init");
                return;
            }
            String sql = new BufferedReader(new InputStreamReader(is))
                .lines().collect(Collectors.joining("\n"));
            Connection conn = connectionPool.acquireConnection();
            try {
                for (String statement : sql.split(";")) {
                    String trimmed = statement.trim();
                    if (!trimmed.isEmpty()) {
                        conn.createStatement().execute(trimmed);
                    }
                }
                logger.info("Database schema initialized successfully");
            } finally {
                connectionPool.releaseConnection(conn);
            }
        } catch (IOException | SQLException e) {
            throw new DatabaseException("Failed to initialize schema: " + e.getMessage(), e);
        }
    }

    @Override
    public void save(QuantityMeasurementEntity entity) {
        Connection conn = connectionPool.acquireConnection();
        try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
            ps.setDouble(1, entity.getFirstQuantity());
            ps.setString(2, entity.getFirstUnit());
            ps.setDouble(3, entity.getSecondQuantity());
            ps.setString(4, entity.getSecondUnit());
            ps.setDouble(5, entity.getResult());
            ps.setString(6, entity.getOperationType());
            ps.setString(7, entity.getMeasurementType());
            ps.executeUpdate();
            logger.info("Saved entity: {} {} -> {} {}",
                entity.getFirstQuantity(), entity.getFirstUnit(),
                entity.getSecondQuantity(), entity.getSecondUnit());
        } catch (SQLException e) {
            throw new DatabaseException("Failed to save entity: " + e.getMessage(), e);
        } finally {
            connectionPool.releaseConnection(conn);
        }
    }

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        Connection conn = connectionPool.acquireConnection();
        List<QuantityMeasurementEntity> results = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                results.add(mapRow(rs));
            }
            logger.info("Retrieved {} measurements", results.size());
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve measurements: " + e.getMessage(), e);
        } finally {
            connectionPool.releaseConnection(conn);
        }
        return results;
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByOperation(String operationType) {
        Connection conn = connectionPool.acquireConnection();
        List<QuantityMeasurementEntity> results = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_BY_OPERATION_SQL)) {
            ps.setString(1, operationType);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) results.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to query by operation: " + e.getMessage(), e);
        } finally {
            connectionPool.releaseConnection(conn);
        }
        return results;
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByType(String measurementType) {
        Connection conn = connectionPool.acquireConnection();
        List<QuantityMeasurementEntity> results = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_BY_TYPE_SQL)) {
            ps.setString(1, measurementType);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) results.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to query by type: " + e.getMessage(), e);
        } finally {
            connectionPool.releaseConnection(conn);
        }
        return results;
    }

    @Override
    public int getTotalCount() {
        Connection conn = connectionPool.acquireConnection();
        try (PreparedStatement ps = conn.prepareStatement(COUNT_SQL);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get count: " + e.getMessage(), e);
        } finally {
            connectionPool.releaseConnection(conn);
        }
        return 0;
    }

    @Override
    public void deleteAll() {
        Connection conn = connectionPool.acquireConnection();
        try (PreparedStatement ps = conn.prepareStatement(DELETE_ALL_SQL)) {
            int deleted = ps.executeUpdate();
            logger.info("Deleted {} measurements", deleted);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete all: " + e.getMessage(), e);
        } finally {
            connectionPool.releaseConnection(conn);
        }
    }

    @Override
    public String getPoolStatistics() {
        return connectionPool.getPoolStatistics();
    }

    @Override
    public void releaseResources() {
        connectionPool.closeAll();
        logger.info("Database resources released");
    }

    private QuantityMeasurementEntity mapRow(ResultSet rs) throws SQLException {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setId(rs.getLong("id"));
        entity.setFirstQuantity(rs.getDouble("first_quantity"));
        entity.setFirstUnit(rs.getString("first_unit"));
        entity.setSecondQuantity(rs.getDouble("second_quantity"));
        entity.setSecondUnit(rs.getString("second_unit"));
        entity.setResult(rs.getDouble("result"));
        entity.setOperationType(rs.getString("operation_type"));
        entity.setMeasurementType(rs.getString("measurement_type"));
        entity.setCreatedAt(rs.getTimestamp("created_at"));
        return entity;
    }
}
