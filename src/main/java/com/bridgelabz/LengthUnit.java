package com.bridgelabz;

public enum LengthUnit {

    FEET(1.0),
    INCHES(1.0 / 12),
    YARDS(3.0);

    private final double feetFactor;

    LengthUnit(double feetFactor) {
        this.feetFactor = feetFactor;
    }

    // Convert given value of this unit to feet
    public double toBase(double value) {
        return value * feetFactor;
    }

    // Convert feet to this unit
    public double fromBase(double baseValue) {
        return baseValue / feetFactor;
    }

    // Convert value from one unit to another
    public static double convert(double value,
                                 LengthUnit from,
                                 LengthUnit to) {

        double baseValue = from.toBase(value);
        return to.fromBase(baseValue);
    }
}