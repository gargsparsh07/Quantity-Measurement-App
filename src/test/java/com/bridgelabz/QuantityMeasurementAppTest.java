package com.bridgelabz;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    @Test
    void testEquality_SameValue() {
        QuantityMeasurementApp.Feet value1 = new QuantityMeasurementApp.Feet(1.0);
        QuantityMeasurementApp.Feet value2 = new QuantityMeasurementApp.Feet(1.0);

        assertTrue(value1.equals(value2));
    }

    @Test
    void testEquality_DifferentValue() {
        QuantityMeasurementApp.Feet value1 = new QuantityMeasurementApp.Feet(1.0);
        QuantityMeasurementApp.Feet value2 = new QuantityMeasurementApp.Feet(2.0);

        assertFalse(value1.equals(value2));
    }

    @Test
    void testEquality_NullComparison() {
        QuantityMeasurementApp.Feet value1 = new QuantityMeasurementApp.Feet(1.0);

        assertFalse(value1.equals(null));
    }

    @Test
    void testEquality_SameReference() {
        QuantityMeasurementApp.Feet value1 = new QuantityMeasurementApp.Feet(1.0);

        assertTrue(value1.equals(value1));
    }
    @Test
    void testEquality_InchesSameValue() {
        QuantityMeasurementApp.Inches i1 = new QuantityMeasurementApp.Inches(1.0);
        QuantityMeasurementApp.Inches i2 = new QuantityMeasurementApp.Inches(1.0);
        assertTrue(i1.equals(i2));
    }
    @Test
    void givenFeetAndInches_WhenEqual_ShouldReturnTrue() {

        QuantityLength oneFoot =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength twelveInches =
                new QuantityLength(12.0, LengthUnit.INCHES);

        assertTrue(oneFoot.equals(twelveInches));
    }
    @Test
    void givenFeetAndYards_WhenEqual_ShouldReturnTrue() {

        QuantityLength threeFeet =
                new QuantityLength(3.0, LengthUnit.FEET);

        QuantityLength oneYard =
                new QuantityLength(1.0, LengthUnit.YARDS);

        assertTrue(threeFeet.equals(oneYard));
    }

    @Test
    void givenCentimeterAndFeet_WhenEqual_ShouldReturnTrue() {

        QuantityLength thirtyCm =
                new QuantityLength(30.48, LengthUnit.CENTIMETERS);

        QuantityLength oneFoot =
                new QuantityLength(1.0, LengthUnit.FEET);

        assertTrue(thirtyCm.equals(oneFoot));
    }
}