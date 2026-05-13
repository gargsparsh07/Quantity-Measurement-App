package com.bridgelabz;

import com.bridgelabz.controller.QuantityMeasurementController;
import com.bridgelabz.exception.QuantityMeasurementException;
import com.bridgelabz.model.QuantityDTO;
import com.bridgelabz.model.QuantityMeasurementEntity;
import com.bridgelabz.repository.IQuantityMeasurementRepository;
import com.bridgelabz.repository.QuantityMeasurementCacheRepository;
import com.bridgelabz.service.IQuantityMeasurementService;
import com.bridgelabz.service.QuantityMeasurementServiceImpl;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class QuantityMeasurementAppTest {

    private IQuantityMeasurementRepository repository;
    private IQuantityMeasurementService    service;
    private QuantityMeasurementController  controller;

    @BeforeEach
    void setUp() {
        repository = QuantityMeasurementCacheRepository.getInstance();
        repository.clearAll();
        service    = new QuantityMeasurementServiceImpl(repository);
        controller = new QuantityMeasurementController(service);
    }

    // ======================================================
    // Entity Tests
    // ======================================================

    @Test @Order(1)
    void testEntity_SingleOperandConstruction() {
        QuantityDTO op1    = new QuantityDTO(1.0,  QuantityDTO.LengthUnit.FEET);
        QuantityDTO result = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES);
        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity("CONVERT", op1, result);

        assertEquals("CONVERT", entity.getOperationType());
        assertEquals(1.0,       entity.getOperand1().getValue());
        assertEquals(12.0,      entity.getResult().getValue());
        assertFalse(entity.hasError());
        assertNull(entity.getOperand2());
    }

    @Test @Order(2)
    void testEntity_BinaryOperandConstruction() {
        QuantityDTO op1    = new QuantityDTO(1.0,  QuantityDTO.LengthUnit.FEET);
        QuantityDTO op2    = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES);
        QuantityDTO result = new QuantityDTO(2.0,  QuantityDTO.LengthUnit.FEET);
        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity("ADD", op1, op2, result);

        assertEquals("ADD", entity.getOperationType());
        assertEquals(1.0,   entity.getOperand1().getValue());
        assertEquals(12.0,  entity.getOperand2().getValue());
        assertEquals(2.0,   entity.getResult().getValue());
        assertFalse(entity.hasError());
    }

    @Test @Order(3)
    void testEntity_ErrorConstruction() {
        QuantityDTO op1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO op2 = new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM);
        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity("COMPARE", op1, op2,
                        "Cross-category not allowed");

        assertTrue(entity.hasError());
        assertEquals("Cross-category not allowed", entity.getErrorMessage());
        assertNull(entity.getResult());
    }

    @Test @Order(4)
    void testEntity_ToString_Success() {
        QuantityDTO op1    = new QuantityDTO(1.0,  QuantityDTO.LengthUnit.FEET);
        QuantityDTO result = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES);
        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity("CONVERT", op1, result);

        assertTrue(entity.toString().contains("CONVERT"));
        assertTrue(entity.toString().contains("=>"));
    }

    @Test @Order(5)
    void testEntity_ToString_Error() {
        QuantityDTO op1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO op2 = new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM);
        QuantityMeasurementEntity entity =
                new QuantityMeasurementEntity("COMPARE", op1, op2, "Error msg");

        assertTrue(entity.toString().contains("ERROR"));
        assertTrue(entity.toString().contains("Error msg"));
    }

    // ======================================================
    // Service Tests — Compare
    // ======================================================

    @Test @Order(6)
    void testService_Compare_SameUnit_Equal() {
        QuantityDTO result = service.compare(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET));
        assertEquals(1.0, result.getValue());
    }

    @Test @Order(7)
    void testService_Compare_DifferentUnit_Equal() {
        QuantityDTO result = service.compare(
                new QuantityDTO(1.0,  QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES));
        assertEquals(1.0, result.getValue());
    }

    @Test @Order(8)
    void testService_Compare_NotEqual() {
        QuantityDTO result = service.compare(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(5.0, QuantityDTO.LengthUnit.INCHES));
        assertEquals(0.0, result.getValue());
    }

    @Test @Order(9)
    void testService_Compare_CrossCategory_ThrowsException() {
        assertThrows(QuantityMeasurementException.class, () ->
                service.compare(
                        new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                        new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM)));
    }

    @Test @Order(10)
    void testService_Compare_Temperature_Equal() {
        QuantityDTO result = service.compare(
                new QuantityDTO(0.0,  QuantityDTO.TemperatureUnit.CELSIUS),
                new QuantityDTO(32.0, QuantityDTO.TemperatureUnit.FAHRENHEIT));
        assertEquals(1.0, result.getValue());
    }

    // ======================================================
    // Service Tests — Convert
    // ======================================================

    @Test @Order(11)
    void testService_Convert_FeetToInches() {
        QuantityDTO result = service.convert(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(0.0, QuantityDTO.LengthUnit.INCHES));
        assertEquals(12.0, result.getValue(), 1e-6);
    }

    @Test @Order(12)
    void testService_Convert_GramToKilogram() {
        QuantityDTO result = service.convert(
                new QuantityDTO(1000.0, QuantityDTO.WeightUnit.GRAM),
                new QuantityDTO(0.0,    QuantityDTO.WeightUnit.KILOGRAM));
        assertEquals(1.0, result.getValue(), 1e-6);
    }

    @Test @Order(13)
    void testService_Convert_CelsiusToFahrenheit() {
        QuantityDTO result = service.convert(
                new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS),
                new QuantityDTO(0.0,   QuantityDTO.TemperatureUnit.FAHRENHEIT));
        assertEquals(212.0, result.getValue(), 0.01);
    }

    // ======================================================
    // Service Tests — Add
    // ======================================================

    @Test @Order(14)
    void testService_Add_FeetAndInches() {
        QuantityDTO result = service.add(
                new QuantityDTO(1.0,  QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES));
        assertEquals(2.0, result.getValue(), 1e-6);
    }

    @Test @Order(15)
    void testService_Add_KilogramAndGram() {
        QuantityDTO result = service.add(
                new QuantityDTO(1.0,   QuantityDTO.WeightUnit.KILOGRAM),
                new QuantityDTO(500.0, QuantityDTO.WeightUnit.GRAM));
        assertEquals(1.5, result.getValue(), 1e-6);
    }

    @Test @Order(16)
    void testService_Add_Temperature_ThrowsException() {
        assertThrows(QuantityMeasurementException.class, () ->
                service.add(
                        new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS),
                        new QuantityDTO(50.0,  QuantityDTO.TemperatureUnit.CELSIUS)));
    }

    // ======================================================
    // Service Tests — Subtract
    // ======================================================

    @Test @Order(17)
    void testService_Subtract_FeetAndInches() {
        QuantityDTO result = service.subtract(
                new QuantityDTO(5.0,  QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(24.0, QuantityDTO.LengthUnit.INCHES));
        assertEquals(3.0, result.getValue(), 1e-6);
    }

    // ======================================================
    // Service Tests — Divide
    // ======================================================

    @Test @Order(18)
    void testService_Divide_TwoFeetByOneFoot() {
        QuantityDTO result = service.divide(
                new QuantityDTO(2.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET));
        assertEquals(2.0, result.getValue(), 1e-6);
    }

    @Test @Order(19)
    void testService_Divide_ByZero_ThrowsException() {
        assertThrows(QuantityMeasurementException.class, () ->
                service.divide(
                        new QuantityDTO(2.0, QuantityDTO.LengthUnit.FEET),
                        new QuantityDTO(0.0, QuantityDTO.LengthUnit.FEET)));
    }

    // ======================================================
    // Controller Tests
    // ======================================================

    @Test @Order(20)
    void testController_Comparison_ReturnsResult() {
        QuantityDTO result = controller.performComparison(
                new QuantityDTO(1.0,  QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES));
        assertNotNull(result);
        assertEquals(1.0, result.getValue());
    }

    @Test @Order(21)
    void testController_CrossCategory_ReturnsNull() {
        QuantityDTO result = controller.performComparison(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM));
        assertNull(result);
    }

    @Test @Order(22)
    void testController_Addition_ReturnsResult() {
        QuantityDTO result = controller.performAddition(
                new QuantityDTO(1.0,  QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES));
        assertNotNull(result);
        assertEquals(2.0, result.getValue(), 1e-6);
    }

    @Test @Order(23)
    void testController_TemperatureAdd_ReturnsNull() {
        QuantityDTO result = controller.performAddition(
                new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS),
                new QuantityDTO(50.0,  QuantityDTO.TemperatureUnit.CELSIUS));
        assertNull(result);
    }

    @Test @Order(24)
    void testController_Division_ReturnsRatio() {
        QuantityDTO result = controller.performDivision(
                new QuantityDTO(2.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET));
        assertNotNull(result);
        assertEquals(2.0, result.getValue(), 1e-6);
    }

    // ======================================================
    // Repository Tests
    // ======================================================

    @Test @Order(25)
    void testRepository_SaveAndRetrieve() {
        service.compare(
                new QuantityDTO(1.0,  QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES));
        assertEquals(1, repository.getAllMeasurements().size());
    }

    @Test @Order(26)
    void testRepository_ClearAll() {
        service.compare(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET));
        repository.clearAll();
        assertEquals(0, repository.getAllMeasurements().size());
    }
}