package com.bridgelabz;

import java.util.Objects;

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

    private void validateSameCategory(Quantity<U> other) {
        if (other == null)
            throw new IllegalArgumentException("Quantity cannot be null");

        if (!this.unit.getClass().equals(other.unit.getClass()))
            throw new IllegalArgumentException("Cross-category operation not allowed");

        if (!Double.isFinite(other.value))
            throw new IllegalArgumentException("Invalid numeric value");
    }

    // ------------------ ADD ------------------

    public Quantity<U> add(Quantity<U> other) {
        validateSameCategory(other);

        double resultBase = this.toBaseUnit() + other.toBaseUnit();
        double result = unit.convertFromBaseUnit(resultBase);

        return new Quantity<>(round(result), unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        validateSameCategory(other);

        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit null");

        double resultBase = this.toBaseUnit() + other.toBaseUnit();
        double result = targetUnit.convertFromBaseUnit(resultBase);

        return new Quantity<>(round(result), targetUnit);
    }

    // ------------------ SUBTRACT ------------------

    public Quantity<U> subtract(Quantity<U> other) {
        validateSameCategory(other);

        double resultBase = this.toBaseUnit() - other.toBaseUnit();
        double result = unit.convertFromBaseUnit(resultBase);

        return new Quantity<>(round(result), unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        validateSameCategory(other);

        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit null");

        double resultBase = this.toBaseUnit() - other.toBaseUnit();
        double result = targetUnit.convertFromBaseUnit(resultBase);

        return new Quantity<>(round(result), targetUnit);
    }

    // ------------------ DIVIDE ------------------

    public double divide(Quantity<U> other) {
        validateSameCategory(other);

        double divisor = other.toBaseUnit();
        if (divisor == 0)
            throw new ArithmeticException("Division by zero");

        return this.toBaseUnit() / divisor;
    }

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