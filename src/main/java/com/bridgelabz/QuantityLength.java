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

    // Convert current quantity to base unit (Feet)
    public double toBaseUnit() {
        return unit.toFeet(value);
    }

    // Convert to another unit
    public static double convert(double value,
                                 LengthUnit from,
                                 LengthUnit to) {

        if (from == null || to == null)
            throw new IllegalArgumentException("Invalid unit");

        double valueInFeet = from.toFeet(value);
        return valueInFeet / to.toFeet(1.0);
    }

    // UC6: Add and return result in current object's unit
    public QuantityLength add(QuantityLength other) {

        if (other == null)
            throw new IllegalArgumentException("Cannot add null");

        double sumInFeet =
                this.toBaseUnit() + other.toBaseUnit();

        double resultValue =
                sumInFeet / this.unit.toFeet(1.0);

        return new QuantityLength(resultValue, this.unit);
    }

    // UC7: Add with explicit target unit
    public QuantityLength add(QuantityLength other,
                              LengthUnit targetUnit) {

        if (other == null || targetUnit == null)
            throw new IllegalArgumentException("Invalid input");

        double sumInFeet =
                this.toBaseUnit() + other.toBaseUnit();

        double resultValue =
                sumInFeet / targetUnit.toFeet(1.0);

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