package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class QuantityMeasurementDatabaseRepositoryTest {

    private QuantityMeasurementDatabaseRepository repository;

    @Before
    public void setUp() {
        repository = new QuantityMeasurementDatabaseRepository();
        repository.deleteAll();
    }

    @After
    public void tearDown() {
        repository.deleteAll();
    }

    @Test
    public void testSaveEntity() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
            1.0, "FEET", 12.0, "INCH", 0.0, "COMPARE", "LENGTH");
        repository.save(entity);
        assertEquals(1, repository.getTotalCount());
    }

    @Test
    public void testGetAllMeasurements() {
        repository.save(new QuantityMeasurementEntity(1.0, "FEET", 12.0, "INCH", 0.0, "COMPARE", "LENGTH"));
        repository.save(new QuantityMeasurementEntity(1.0, "KG", 1000.0, "GRAM", 0.0, "COMPARE", "WEIGHT"));
        List<QuantityMeasurementEntity> all = repository.getAllMeasurements();
        assertEquals(2, all.size());
    }

    @Test
    public void testGetMeasurementsByOperation() {
        repository.save(new QuantityMeasurementEntity(1.0, "FEET", 12.0, "INCH", 0.0, "COMPARE", "LENGTH"));
        repository.save(new QuantityMeasurementEntity(2.0, "FEET", 2.0, "FEET", 4.0, "ADD", "LENGTH"));
        List<QuantityMeasurementEntity> compareOps = repository.getMeasurementsByOperation("COMPARE");
        assertEquals(1, compareOps.size());
    }

    @Test
    public void testGetMeasurementsByType() {
        repository.save(new QuantityMeasurementEntity(1.0, "FEET", 12.0, "INCH", 0.0, "COMPARE", "LENGTH"));
        repository.save(new QuantityMeasurementEntity(1.0, "KG", 1000.0, "GRAM", 0.0, "COMPARE", "WEIGHT"));
        List<QuantityMeasurementEntity> lengthMeasurements = repository.getMeasurementsByType("LENGTH");
        assertEquals(1, lengthMeasurements.size());
    }

    @Test
    public void testDeleteAll() {
        repository.save(new QuantityMeasurementEntity(1.0, "FEET", 12.0, "INCH", 0.0, "COMPARE", "LENGTH"));
        repository.deleteAll();
        assertEquals(0, repository.getTotalCount());
    }

    @Test
    public void testGetTotalCount() {
        assertEquals(0, repository.getTotalCount());
        repository.save(new QuantityMeasurementEntity(1.0, "FEET", 12.0, "INCH", 0.0, "COMPARE", "LENGTH"));
        assertEquals(1, repository.getTotalCount());
    }

    @Test
    public void testPoolStatistics() {
        String stats = repository.getPoolStatistics();
        assertNotNull(stats);
        assertTrue(stats.contains("Pool"));
    }
}
