package com.bridgelabz;

import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

public class Quantity<U extends IMeasurable> {

    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {

        if (unit == null)
            throw new IllegalArgumentException("Unit cannot be null");

        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Invalid numeric value");

        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    private double toBaseUnit() {
        return unit.convertToBaseUnit(value);
    }

    // ================== ENUM ==================

    private enum ArithmeticOperation {

        ADD((a, b) -> a + b),
        SUBTRACT((a, b) -> a - b),
        DIVIDE((a, b) -> {
            if (b == 0)
                throw new ArithmeticException("Division by zero");
            return a / b;
        });

        private final DoubleBinaryOperator operator;

        ArithmeticOperation(DoubleBinaryOperator operator) {
            this.operator = operator;
        }

        double compute(double a, double b) {
            return operator.applyAsDouble(a, b);
        }
    }

    // ================== VALIDATION ==================

    private void validateOperands(Quantity<U> other, U targetUnit, boolean targetRequired) {

        if (other == null)
            throw new IllegalArgumentException("Quantity cannot be null");

        if (!this.unit.getClass().equals(other.unit.getClass()))
            throw new IllegalArgumentException("Cross-category operation not allowed");

        if (!Double.isFinite(other.value))
            throw new IllegalArgumentException("Invalid numeric value");

        if (targetRequired && targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");
    }

    // ================== CORE HELPER ==================

    private double performBaseArithmetic(Quantity<U> other,
                                         ArithmeticOperation operation) {

        double base1 = this.toBaseUnit();
        double base2 = other.toBaseUnit();

        return operation.compute(base1, base2);
    }

    // ================== ADD ==================

    public Quantity<U> add(Quantity<U> other) {
        validateOperands(other, unit, false);

        double resultBase =
                performBaseArithmetic(other, ArithmeticOperation.ADD);

        double result = unit.convertFromBaseUnit(resultBase);

        return new Quantity<>(round(result), unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        validateOperands(other, targetUnit, true);

        double resultBase =
                performBaseArithmetic(other, ArithmeticOperation.ADD);

        double result = targetUnit.convertFromBaseUnit(resultBase);

        return new Quantity<>(round(result), targetUnit);
    }

    // ================== SUBTRACT ==================

    public Quantity<U> subtract(Quantity<U> other) {
        validateOperands(other, unit, false);

        double resultBase =
                performBaseArithmetic(other, ArithmeticOperation.SUBTRACT);

        double result = unit.convertFromBaseUnit(resultBase);

        return new Quantity<>(round(result), unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        validateOperands(other, targetUnit, true);

        double resultBase =
                performBaseArithmetic(other, ArithmeticOperation.SUBTRACT);

        double result = targetUnit.convertFromBaseUnit(resultBase);

        return new Quantity<>(round(result), targetUnit);
    }

    // ================== DIVIDE ==================

    public double divide(Quantity<U> other) {
        validateOperands(other, null, false);

        return performBaseArithmetic(other, ArithmeticOperation.DIVIDE);
    }

    // ================== UTILITY ==================

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Quantity<?> other)) return false;

        if (!this.unit.getClass().equals(other.unit.getClass()))
            return false;

        return Math.abs(this.toBaseUnit() - other.toBaseUnit()) < 1e-6;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Math.round(toBaseUnit() * 1e6));
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}