package com.bridgelabz.repository;

import com.bridgelabz.model.QuantityMeasurementEntity;
import java.util.List;

/**
 * UC15: IQuantityMeasurementRepository - Repository Interface
 * Defines data-access contract for QuantityMeasurementEntity persistence.
 * Follows Interface Segregation Principle.
 */
public interface IQuantityMeasurementRepository {

    /**
     * Saves a QuantityMeasurementEntity to the underlying store.
     */
    void save(QuantityMeasurementEntity entity);

    /**
     * Returns all saved measurement records.
     */
    List<QuantityMeasurementEntity> getAllMeasurements();

    /**
     * Clears all stored measurement records (useful for testing).
     */
    void clearAll();
}