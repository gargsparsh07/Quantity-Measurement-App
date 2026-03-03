package com.bridgelabz;

public interface IMeasurable {

    double convertToBaseUnit(double value);

    double convertFromBaseUnit(double baseValue);

    // ------------------ Arithmetic Capability ------------------

    SupportsArithmetic supportsArithmetic = () -> true;

    default boolean supportsArithmetic() {
        return supportsArithmetic.isSupported();
    }

    default void validateOperationSupport(String operation) {
        // Default: all operations supported
    }
}