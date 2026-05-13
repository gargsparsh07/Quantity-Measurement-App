package com.bridgelabz.controller;

import com.bridgelabz.exception.QuantityMeasurementException;
import com.bridgelabz.model.QuantityDTO;
import com.bridgelabz.service.IQuantityMeasurementService;

/**
 * UC15: QuantityMeasurementController - Controller Layer
 * Bridge between application entry point and service layer.
 * Delegates all business logic to IQuantityMeasurementService.
 */
public class QuantityMeasurementController {

    // ======== Dependency ========
    private final IQuantityMeasurementService service;

    public QuantityMeasurementController(IQuantityMeasurementService service) {
        if (service == null)
            throw new IllegalArgumentException("Service cannot be null");
        this.service = service;
    }

    // ======== API Methods ========

    // POST /api/quantity/compare
    public QuantityDTO performComparison(QuantityDTO q1, QuantityDTO q2) {
        try {
            QuantityDTO result = service.compare(q1, q2);
            displayResult("COMPARE", q1, q2, result);
            return result;
        } catch (QuantityMeasurementException e) {
            displayError("COMPARE", e.getMessage());
            return null;
        }
    }

    // POST /api/quantity/convert
    public QuantityDTO performConversion(QuantityDTO source,
                                         QuantityDTO targetUnit) {
        try {
            QuantityDTO result = service.convert(source, targetUnit);
            displayResult("CONVERT", source, targetUnit, result);
            return result;
        } catch (QuantityMeasurementException e) {
            displayError("CONVERT", e.getMessage());
            return null;
        }
    }

    // POST /api/quantity/add
    public QuantityDTO performAddition(QuantityDTO q1, QuantityDTO q2) {
        try {
            QuantityDTO result = service.add(q1, q2);
            displayResult("ADD", q1, q2, result);
            return result;
        } catch (QuantityMeasurementException e) {
            displayError("ADD", e.getMessage());
            return null;
        }
    }

    // POST /api/quantity/subtract
    public QuantityDTO performSubtraction(QuantityDTO q1, QuantityDTO q2) {
        try {
            QuantityDTO result = service.subtract(q1, q2);
            displayResult("SUBTRACT", q1, q2, result);
            return result;
        } catch (QuantityMeasurementException e) {
            displayError("SUBTRACT", e.getMessage());
            return null;
        }
    }

    // POST /api/quantity/divide
    public QuantityDTO performDivision(QuantityDTO q1, QuantityDTO q2) {
        try {
            QuantityDTO result = service.divide(q1, q2);
            displayResult("DIVIDE", q1, q2, result);
            return result;
        } catch (QuantityMeasurementException e) {
            displayError("DIVIDE", e.getMessage());
            return null;
        }
    }

    // ======== Display Helpers ========
    private void displayResult(String operation,
                               QuantityDTO q1,
                               QuantityDTO q2,
                               QuantityDTO result) {
        System.out.println("--------------------------------------------------");
        System.out.println("Operation : " + operation);
        System.out.println("Operand 1 : " + formatDTO(q1));
        if (q2 != null)
            System.out.println("Operand 2 : " + formatDTO(q2));
        System.out.println("Result    : " + formatResult(operation, result));
        System.out.println("--------------------------------------------------");
    }

    private void displayError(String operation, String message) {
        System.out.println("--------------------------------------------------");
        System.out.println("Operation : " + operation);
        System.out.println("ERROR     : " + message);
        System.out.println("--------------------------------------------------");
    }

    private String formatDTO(QuantityDTO dto) {
        if (dto == null) return "null";
        String unit = (dto.getUnit() != null)
                ? dto.getUnit().getUnitName() : "dimensionless";
        return dto.getValue() + " " + unit;
    }

    private String formatResult(String operation, QuantityDTO result) {
        if (result == null) return "null";
        if ("COMPARE".equals(operation))
            return result.getValue() == 1.0 ? "EQUAL" : "NOT EQUAL";
        if ("DIVIDE".equals(operation))
            return "ratio = " + result.getValue();
        String unit = (result.getUnit() != null)
                ? result.getUnit().getUnitName() : "dimensionless";
        return result.getValue() + " " + unit;
    }
}