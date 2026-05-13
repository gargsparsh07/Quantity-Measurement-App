package com.app.quantitymeasurement;

import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityInputDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuantityMeasurementApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1/quantities";
    }

    private QuantityInputDTO buildInput(double v1, String u1, String t1,
                                        double v2, String u2, String t2) {
        QuantityDTO q1 = new QuantityDTO();
        q1.setValue(v1); q1.setUnit(u1); q1.setMeasurementType(t1);

        QuantityDTO q2 = new QuantityDTO();
        q2.setValue(v2); q2.setUnit(u2); q2.setMeasurementType(t2);

        QuantityInputDTO input = new QuantityInputDTO();
        input.setThisQuantityDTO(q1);
        input.setThatQuantityDTO(q2);
        return input;
    }

    @Test
    void contextLoads() {}

    @Test
    void testCompareQuantities_FeetToInch() {
        QuantityInputDTO input = buildInput(1.0, "FEET", "LengthUnit", 12.0, "INCH", "LengthUnit");
        ResponseEntity<QuantityMeasurementDTO> response =
            restTemplate.postForEntity(baseUrl + "/compare", input, QuantityMeasurementDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("true", response.getBody().getResultString());
    }

    @Test
    void testAddQuantities_FeetPlusFeet() {
        QuantityInputDTO input = buildInput(2.0, "FEET", "LengthUnit", 2.0, "FEET", "LengthUnit");
        ResponseEntity<QuantityMeasurementDTO> response =
            restTemplate.postForEntity(baseUrl + "/add", input, QuantityMeasurementDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(4.0, response.getBody().getResultValue(), 0.001);
    }

    @Test
    void testConvertQuantity_FeetToInch() {
        QuantityInputDTO input = buildInput(1.0, "FEET", "LengthUnit", 0.0, "INCH", "LengthUnit");
        ResponseEntity<QuantityMeasurementDTO> response =
            restTemplate.postForEntity(baseUrl + "/convert", input, QuantityMeasurementDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(12.0, response.getBody().getResultValue(), 0.001);
    }

    @Test
    void testGetHistoryByOperation() {
        QuantityInputDTO input = buildInput(1.0, "FEET", "LengthUnit", 12.0, "INCH", "LengthUnit");
        restTemplate.postForEntity(baseUrl + "/compare", input, QuantityMeasurementDTO.class);

        ResponseEntity<QuantityMeasurementDTO[]> response =
            restTemplate.getForEntity(baseUrl + "/history/operation/compare", QuantityMeasurementDTO[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length >= 1);
    }

    @Test
    void testGetOperationCount() {
        ResponseEntity<Long> response =
            restTemplate.getForEntity(baseUrl + "/count/COMPARE", Long.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetErrorHistory() {
        ResponseEntity<QuantityMeasurementDTO[]> response =
            restTemplate.getForEntity(baseUrl + "/history/errored", QuantityMeasurementDTO[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testActuatorHealth() {
        ResponseEntity<String> response =
            restTemplate.getForEntity("http://localhost:" + port + "/actuator/health", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("UP"));
    }
}
