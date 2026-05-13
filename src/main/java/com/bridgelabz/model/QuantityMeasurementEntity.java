package com.bridgelabz.model;

import java.io.Serializable;

/**
 * UC15: QuantityMeasurementEntity - Persistence POJO
 * Stores operands, operation type, result and error details.
 * Implements Serializable for disk-based persistence.
 */
public class QuantityMeasurementEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    // ======== Fields ========
    private QuantityDTO operand1;
    private QuantityDTO operand2;
    private QuantityDTO result;
    private String      operationType;
    private boolean     hasError;
    private String      errorMessage;

    // ======== Constructors ========
    public QuantityMeasurementEntity() {}

    // Single operand (e.g. conversion)
    public QuantityMeasurementEntity(String operationType,
                                     QuantityDTO operand1,
                                     QuantityDTO result) {
        this.operationType = operationType;
        this.operand1      = operand1;
        this.result        = result;
        this.hasError      = false;
    }

    // Binary operand (e.g. compare, add, subtract, divide)
    public QuantityMeasurementEntity(String operationType,
                                     QuantityDTO operand1,
                                     QuantityDTO operand2,
                                     QuantityDTO result) {
        this.operationType = operationType;
        this.operand1      = operand1;
        this.operand2      = operand2;
        this.result        = result;
        this.hasError      = false;
    }

    // Error state
    public QuantityMeasurementEntity(String operationType,
                                     QuantityDTO operand1,
                                     QuantityDTO operand2,
                                     String errorMessage) {
        this.operationType = operationType;
        this.operand1      = operand1;
        this.operand2      = operand2;
        this.hasError      = true;
        this.errorMessage  = errorMessage;
    }

    // ======== Getters ========
    public QuantityDTO getOperand1()      { return operand1; }
    public QuantityDTO getOperand2()      { return operand2; }
    public QuantityDTO getResult()        { return result; }
    public String      getOperationType() { return operationType; }
    public boolean     hasError()         { return hasError; }
    public String      getErrorMessage()  { return errorMessage; }

    @Override
    public String toString() {
        if (hasError)
            return "[" + operationType + "] ERROR: " + errorMessage;
        return "[" + operationType + "] "
                + operand1
                + (operand2 != null ? " , " + operand2 : "")
                + " => " + result;
    }
}