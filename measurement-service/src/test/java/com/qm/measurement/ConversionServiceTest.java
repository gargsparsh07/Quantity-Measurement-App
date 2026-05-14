package com.qm.measurement;

import com.qm.measurement.client.UserServiceClient;
import com.qm.measurement.model.ConversionResult;
import com.qm.measurement.service.ConversionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class ConversionServiceTest {

    @InjectMocks
    private ConversionService conversionService;

    @Mock
    private UserServiceClient userServiceClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConvertLength_kmToMiles() {
        ConversionResult result = conversionService.convertLength("km", "miles", 10.0, null);
        assertEquals(6.21371, result.getResultValue(), 0.001);
        assertEquals("LENGTH", result.getType());
    }

    @Test
    void testConvertLength_feetToInch() {
        ConversionResult result = conversionService.convertLength("feet", "inch", 1.0, null);
        assertEquals(12.0, result.getResultValue(), 0.001);
    }

    @Test
    void testConvertWeight_kgToLbs() {
        ConversionResult result = conversionService.convertWeight("kg", "lbs", 1.0, null);
        assertEquals(2.20462, result.getResultValue(), 0.001);
    }

    @Test
    void testConvertTemperature_CtoF() {
        ConversionResult result = conversionService.convertTemperature("C", "F", 100.0, null);
        assertEquals(212.0, result.getResultValue(), 0.001);
    }

    @Test
    void testConvertVolume_literToGallon() {
        ConversionResult result = conversionService.convertVolume("liter", "gallon", 1.0, null);
        assertEquals(0.264172, result.getResultValue(), 0.001);
    }

    @Test
    void testConvertLength_unsupported_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
            conversionService.convertLength("km", "parsec", 1.0, null));
    }
}
