package com.bridgelabz.exception;

/**
 * UC15: Custom Runtime Exception for Quantity Measurement operations.
 */
public class QuantityMeasurementException extends RuntimeException {

    public QuantityMeasurementException(String message) {
        super(message);
    }

    public QuantityMeasurementException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuantityMeasurementException(Throwable cause) {
        super(cause);
    }
}