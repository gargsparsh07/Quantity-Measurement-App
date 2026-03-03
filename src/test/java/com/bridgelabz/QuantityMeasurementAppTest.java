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
    @Test
    void givenFeet_WhenConvertedToInches_ShouldReturn12() {

        double result = QuantityLength.convert(
                1.0,
                LengthUnit.FEET,
                LengthUnit.INCHES
        );

        assertEquals(12.0, result, 1e-6);
    }

    @Test
    void givenYard_WhenConvertedToFeet_ShouldReturn3() {

        double result = QuantityLength.convert(
                1.0,
                LengthUnit.YARDS,
                LengthUnit.FEET
        );

        assertEquals(3.0, result, 1e-6);
    }

    @Test
    void givenCentimeter_WhenConvertedToFeet_ShouldReturn1() {

        double result = QuantityLength.convert(
                30.48,
                LengthUnit.CENTIMETERS,
                LengthUnit.FEET
        );

        assertEquals(1.0, result, 1e-4);
    }
    @Test
    void givenFeetAndInches_WhenAdded_ShouldReturn2Feet() {

        QuantityLength oneFoot =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength twelveInches =
                new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength result = oneFoot.add(twelveInches);

        assertEquals(2.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test
    void givenYardAndFoot_WhenAdded_ShouldReturn4Feet() {

        QuantityLength oneYard =
                new QuantityLength(1.0, LengthUnit.YARDS);

        QuantityLength oneFoot =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength result = oneYard.add(oneFoot);

        double resultInFeet = QuantityLength.convert(
                result.getValue(),
                result.getUnit(),
                LengthUnit.FEET
        );

        assertEquals(4.0, resultInFeet, 1e-6);
    }
}