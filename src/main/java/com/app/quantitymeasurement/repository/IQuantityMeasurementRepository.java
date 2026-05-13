package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;

import java.util.List;

public interface IQuantityMeasurementRepository {

    void save(QuantityMeasurementEntity entity);

    List<QuantityMeasurementEntity> getAllMeasurements();

    List<QuantityMeasurementEntity> getMeasurementsByOperation(String operationType);

    List<QuantityMeasurementEntity> getMeasurementsByType(String measurementType);

    int getTotalCount();

    void deleteAll();

    default String getPoolStatistics() {
        return "Pool statistics not available for this repository";
    }

    default void releaseResources() {
        // Default no-op; override in implementations that hold resources
    }
}
