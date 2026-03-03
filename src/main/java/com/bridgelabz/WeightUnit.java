package com.bridgelabz;

public enum WeightUnit {

    KILOGRAM(1.0),
    GRAM(0.001),
    TON(1000.0);

    private final double kgFactor;

    WeightUnit(double kgFactor) {
        this.kgFactor = kgFactor;
    }

    // Convert to base unit (KG)
    public double toBase(double value) {
        return value * kgFactor;
    }

    // Convert from base (KG) to this unit
    public double fromBase(double baseValue) {
        return baseValue / kgFactor;
    }

    public static double convert(double value,
                                 WeightUnit from,
                                 WeightUnit to) {

        double baseValue = from.toBase(value);
        return to.fromBase(baseValue);
    }
}
