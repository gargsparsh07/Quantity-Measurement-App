package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.model.QuantityInputDTO;
import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import com.app.quantitymeasurement.services.IQuantityMeasurementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuantityMeasurementController.class)
@Import(com.app.quantitymeasurement.config.SecurityConfig.class)
class QuantityMeasurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IQuantityMeasurementService service;

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
    void testCompareQuantities_Returns200() throws Exception {
        QuantityInputDTO input = buildInput(1.0, "FEET", "LengthUnit", 12.0, "INCH", "LengthUnit");

        QuantityMeasurementDTO mockResult = new QuantityMeasurementDTO();
        mockResult.setResultString("true");
        mockResult.setOperation("compare");

        Mockito.when(service.compareQuantities(Mockito.any())).thenReturn(mockResult);

        mockMvc.perform(post("/api/v1/quantities/compare")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.resultString").value("true"));
    }

    @Test
    void testAddQuantities_Returns200() throws Exception {
        QuantityInputDTO input = buildInput(2.0, "FEET", "LengthUnit", 2.0, "FEET", "LengthUnit");

        QuantityMeasurementDTO mockResult = new QuantityMeasurementDTO();
        mockResult.setResultValue(4.0);
        mockResult.setOperation("add");

        Mockito.when(service.addQuantities(Mockito.any())).thenReturn(mockResult);

        mockMvc.perform(post("/api/v1/quantities/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.resultValue").value(4.0));
    }

    @Test
    void testGetOperationHistory_Returns200() throws Exception {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setOperation("compare");
        Mockito.when(service.getHistoryByOperation("COMPARE")).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/quantities/history/operation/COMPARE"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].operation").value("compare"));
    }

    @Test
    void testGetOperationCount_Returns200() throws Exception {
        Mockito.when(service.getOperationCount("COMPARE")).thenReturn(3L);

        mockMvc.perform(get("/api/v1/quantities/count/COMPARE"))
            .andExpect(status().isOk())
            .andExpect(content().string("3"));
    }

    @Test
    void testInvalidInput_Returns400() throws Exception {
        String badJson = "{ \"thisQuantityDTO\": { \"value\": null, \"unit\": \"\", \"measurementType\": \"\" }, " +
                         "\"thatQuantityDTO\": { \"value\": 1.0, \"unit\": \"INCH\", \"measurementType\": \"LengthUnit\" } }";

        mockMvc.perform(post("/api/v1/quantities/compare")
                .contentType(MediaType.APPLICATION_JSON)
                .content(badJson))
            .andExpect(status().isBadRequest());
    }
}
