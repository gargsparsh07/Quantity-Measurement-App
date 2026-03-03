package com.bridgelabz;

import java.util.Objects;

public class QuantityLength {

    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {

        if (unit == null)
            throw new IllegalArgumentException("Unit cannot be null");

        this.value = value;
        this.unit = unit;
    }

    // Convert this quantity to base unit (feet)
    public double toBaseUnit() {
        return unit.toBase(value);
    }

    // Convert to another unit
    public QuantityLength convertTo(LengthUnit targetUnit) {

        if (targetUnit == null)
            throw new IllegalArgumentException("Invalid target unit");

        double baseValue = this.toBaseUnit();
        double convertedValue = targetUnit.fromBase(baseValue);

        return new QuantityLength(convertedValue, targetUnit);
    }

    // UC6
    public QuantityLength add(QuantityLength other) {

        if (other == null)
            throw new IllegalArgumentException("Cannot add null");

        double sumBase =
                this.toBaseUnit() + other.toBaseUnit();

        double resultValue =
                unit.fromBase(sumBase);

        return new QuantityLength(resultValue, unit);
    }

    // UC7
    public QuantityLength add(QuantityLength other,
                              LengthUnit targetUnit) {

        if (other == null || targetUnit == null)
            throw new IllegalArgumentException("Invalid input");

        double sumBase =
                this.toBaseUnit() + other.toBaseUnit();

        double resultValue =
                targetUnit.fromBase(sumBase);

        return new QuantityLength(resultValue, targetUnit);
    }

    public double getValue() {
        return value;
    }

    public LengthUnit getUnit() {
        return unit;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (!(obj instanceof QuantityLength))
            return false;

        QuantityLength other = (QuantityLength) obj;

        return Math.abs(this.toBaseUnit()
                - other.toBaseUnit()) < 1e-6;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                Math.round(toBaseUnit() * 1000000)
        );
    }
}