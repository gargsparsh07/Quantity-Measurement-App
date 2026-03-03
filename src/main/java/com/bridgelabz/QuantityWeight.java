package com.bridgelabz;

import java.util.Objects;

public class QuantityWeight {

    private final double value;
    private final WeightUnit unit;

    public QuantityWeight(double value, WeightUnit unit) {

        if (unit == null)
            throw new IllegalArgumentException("Unit cannot be null");

        this.value = value;
        this.unit = unit;
    }

    public double toBaseUnit() {
        return unit.toBase(value);
    }

    public QuantityWeight convertTo(WeightUnit targetUnit) {

        if (targetUnit == null)
            throw new IllegalArgumentException("Invalid unit");

        double baseValue = this.toBaseUnit();
        double convertedValue = targetUnit.fromBase(baseValue);

        return new QuantityWeight(convertedValue, targetUnit);
    }

    public QuantityWeight add(QuantityWeight other) {

        if (other == null)
            throw new IllegalArgumentException("Cannot add null");

        double sumBase =
                this.toBaseUnit() + other.toBaseUnit();

        double resultValue =
                unit.fromBase(sumBase);

        return new QuantityWeight(resultValue, unit);
    }

    public QuantityWeight add(QuantityWeight other,
                              WeightUnit targetUnit) {

        if (other == null || targetUnit == null)
            throw new IllegalArgumentException("Invalid input");

        double sumBase =
                this.toBaseUnit() + other.toBaseUnit();

        double resultValue =
                targetUnit.fromBase(sumBase);

        return new QuantityWeight(resultValue, targetUnit);
    }

    public double getValue() {
        return value;
    }

    public WeightUnit getUnit() {
        return unit;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (!(obj instanceof QuantityWeight))
            return false;

        QuantityWeight other = (QuantityWeight) obj;

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