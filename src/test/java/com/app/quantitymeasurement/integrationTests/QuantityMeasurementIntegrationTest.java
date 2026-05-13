package com.app.quantitymeasurement.integrationTests;

import com.app.quantitymeasurement.QuantityMeasurementApp;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class QuantityMeasurementIntegrationTest {

    private QuantityMeasurementApp app;

    @Before
    public void setUp() {
        app = new QuantityMeasurementApp();
        app.deleteAllMeasurements();
    }

    @After
    public void tearDown() {
        app.deleteAllMeasurements();
        app.closeResources();
    }

    @Test
    public void testCompareAndPersist_FeetToInch() {
        boolean result = app.getController().compareQuantities(1.0, "FEET", 12.0, "INCH");
        assertTrue(result);
        List<QuantityMeasurementEntity> all = app.getAllMeasurements();
        assertFalse(all.isEmpty());
    }

    @Test
    public void testCompareAndPersist_KgToGram() {
        boolean result = app.getController().compareQuantities(1.0, "KG", 1000.0, "GRAM");
        assertTrue(result);
        assertEquals(1, app.getAllMeasurements().size());
    }

    @Test
    public void testAddAndPersist_FeetPlusFeet() {
        double result = app.getController().addQuantities(2.0, "FEET", 2.0, "FEET", "FEET");
        assertEquals(4.0, result, 0.001);
        assertEquals(1, app.getAllMeasurements().size());
    }

    @Test
    public void testMultipleOperationsPersisted() {
        app.getController().compareQuantities(1.0, "FEET", 12.0, "INCH");
        app.getController().compareQuantities(1.0, "KG", 1000.0, "GRAM");
        app.getController().addQuantities(2.0, "FEET", 2.0, "FEET", "FEET");
        assertEquals(3, app.getAllMeasurements().size());
    }

    @Test
    public void testDeleteAllMeasurements() {
        app.getController().compareQuantities(1.0, "FEET", 12.0, "INCH");
        app.deleteAllMeasurements();
        assertEquals(0, app.getAllMeasurements().size());
    }
}
