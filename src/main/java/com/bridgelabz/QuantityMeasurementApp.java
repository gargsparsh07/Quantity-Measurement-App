package com.bridgelabz;

public class QuantityMeasurementApp {

    public static <U extends IMeasurable>
    void demonstrateEquality(Quantity<U> q1,
                             Quantity<U> q2) {

        System.out.println(q1 + " equals "
                + q2 + " : "
                + q1.equals(q2));
    }

    public static <U extends IMeasurable>
    void demonstrateConversion(Quantity<U> q,
                               U targetUnit) {

        System.out.println(q + " converts to "
                + q.convertTo(targetUnit));
    }

    public static <U extends IMeasurable>
    void demonstrateAddition(Quantity<U> q1,
                             Quantity<U> q2,
                             U targetUnit) {

        System.out.println(q1 + " + "
                + q2 + " = "
                + q1.add(q2, targetUnit));
    }

    public static void main(String[] args) {

        // Length Demo
        Quantity<LengthUnit> length1 =
                new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> length2 =
                new Quantity<>(12.0, LengthUnit.INCHES);

        System.out.println("Length Equal: " +
                length1.equals(length2));

        // Weight Demo
        Quantity<WeightUnit> weight1 =
                new Quantity<>(1.0, WeightUnit.KILOGRAM);

        Quantity<WeightUnit> weight2 =
                new Quantity<>(1000.0, WeightUnit.GRAM);

        System.out.println("Weight Equal: " +
                weight1.equals(weight2));

        // 🔥 UC11 Volume Demo
        Quantity<VolumeUnit> v1 =
                new Quantity<>(1.0, VolumeUnit.LITRE);

        Quantity<VolumeUnit> v2 =
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        System.out.println("Volume Equal: " +
                v1.equals(v2));
    }
}