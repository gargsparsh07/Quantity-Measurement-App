package com.bridgelabz;

public class QuantityLength {

    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {
        if (!Double.isFinite(value) || unit == null)
            throw new IllegalArgumentException("Invalid input");
        this.value = value;
        this.unit = unit;
    }

    private double toBaseUnit() {
        return unit.toFeet(value);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        QuantityLength other = (QuantityLength) obj;

        return Double.compare(this.toBaseUnit(), other.toBaseUnit()) == 0;
    }
    public static double convert(double value,
                                 LengthUnit source,
                                 LengthUnit target) {

        if (!Double.isFinite(value) || source == null || target == null) {
            throw new IllegalArgumentException("Invalid input");
        }

        // Convert source value to feet first (base unit)
        double valueInFeet = source.toFeet(value);

        // Convert feet to target unit
        return valueInFeet / target.toFeet(1.0);
    }
    public QuantityLength add(QuantityLength other) {

        if (other == null) {
            throw new IllegalArgumentException("Cannot add null quantity");
        }

        // Convert both to base unit (feet)
        double sumInFeet = this.toBaseUnit() + other.toBaseUnit();

        // Convert back to the unit of current object
        double resultValue = sumInFeet / this.unit.toFeet(1.0);

        return new QuantityLength(resultValue, this.unit);
    }
    public double getValue() {
        return value;
    }

    public LengthUnit getUnit() {
        return unit;
    }
}