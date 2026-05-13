package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.model.QuantityInputDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import com.app.quantitymeasurement.services.IQuantityMeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quantities")
@Tag(name = "Quantity Measurements", description = "REST API for quantity measurement operations")
public class QuantityMeasurementController {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementController.class);

    @Autowired
    private IQuantityMeasurementService service;

    @PostMapping("/compare")
    @Operation(summary = "Compare two quantities")
    public ResponseEntity<QuantityMeasurementDTO> compareQuantities(@Valid @RequestBody QuantityInputDTO input) {
        logger.info("POST /compare");
        return ResponseEntity.ok(service.compareQuantities(input));
    }

    @PostMapping("/convert")
    @Operation(summary = "Convert a quantity to another unit")
    public ResponseEntity<QuantityMeasurementDTO> convertQuantity(@Valid @RequestBody QuantityInputDTO input) {
        logger.info("POST /convert");
        return ResponseEntity.ok(service.convertQuantity(input));
    }

    @PostMapping("/add")
    @Operation(summary = "Add two quantities")
    public ResponseEntity<QuantityMeasurementDTO> addQuantities(@Valid @RequestBody QuantityInputDTO input) {
        logger.info("POST /add");
        return ResponseEntity.ok(service.addQuantities(input));
    }

    @PostMapping("/subtract")
    @Operation(summary = "Subtract two quantities")
    public ResponseEntity<QuantityMeasurementDTO> subtractQuantities(@Valid @RequestBody QuantityInputDTO input) {
        logger.info("POST /subtract");
        return ResponseEntity.ok(service.subtractQuantities(input));
    }

    @PostMapping("/multiply")
    @Operation(summary = "Multiply two quantities")
    public ResponseEntity<QuantityMeasurementDTO> multiplyQuantities(@Valid @RequestBody QuantityInputDTO input) {
        logger.info("POST /multiply");
        return ResponseEntity.ok(service.multiplyQuantities(input));
    }

    @PostMapping("/divide")
    @Operation(summary = "Divide two quantities")
    public ResponseEntity<QuantityMeasurementDTO> divideQuantities(@Valid @RequestBody QuantityInputDTO input) {
        logger.info("POST /divide");
        return ResponseEntity.ok(service.divideQuantities(input));
    }

    @GetMapping("/history/operation/{operation}")
    @Operation(summary = "Get measurement history by operation type")
    public ResponseEntity<List<QuantityMeasurementDTO>> getOperationHistory(@PathVariable String operation) {
        logger.info("GET /history/operation/{}", operation);
        return ResponseEntity.ok(service.getHistoryByOperation(operation));
    }

    @GetMapping("/history/type/{measurementType}")
    @Operation(summary = "Get measurement history by measurement type")
    public ResponseEntity<List<QuantityMeasurementDTO>> getHistoryByType(@PathVariable String measurementType) {
        logger.info("GET /history/type/{}", measurementType);
        return ResponseEntity.ok(service.getHistoryByMeasurementType(measurementType));
    }

    @GetMapping("/history/errored")
    @Operation(summary = "Get all errored measurements")
    public ResponseEntity<List<QuantityMeasurementDTO>> getErrorHistory() {
        logger.info("GET /history/errored");
        return ResponseEntity.ok(service.getErrorHistory());
    }

    @GetMapping("/count/{operation}")
    @Operation(summary = "Get count of successful operations by type")
    public ResponseEntity<Long> getOperationCount(@PathVariable String operation) {
        logger.info("GET /count/{}", operation);
        return ResponseEntity.ok(service.getOperationCount(operation));
    }
}
