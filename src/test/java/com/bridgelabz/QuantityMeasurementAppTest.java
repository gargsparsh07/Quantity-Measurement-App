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
    @Test
    void testAddition_ExplicitTargetUnit_Feet() {

        QuantityLength result =
                new QuantityLength(1.0, LengthUnit.FEET)
                        .add(new QuantityLength(12.0, LengthUnit.INCHES),
                                LengthUnit.FEET);

        assertEquals(2.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }
    @Test
    void testAddition_ExplicitTargetUnit_Inches() {

        QuantityLength result =
                new QuantityLength(1.0, LengthUnit.FEET)
                        .add(new QuantityLength(12.0, LengthUnit.INCHES),
                                LengthUnit.INCHES);

        assertEquals(24.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.INCHES, result.getUnit());
    }
    @Test
    void testAddition_ExplicitTargetUnit_Yards() {

        QuantityLength result =
                new QuantityLength(1.0, LengthUnit.FEET)
                        .add(new QuantityLength(12.0, LengthUnit.INCHES),
                                LengthUnit.YARDS);

        assertEquals(0.6667, result.getValue(), 1e-3);
        assertEquals(LengthUnit.YARDS, result.getUnit());
    }
    @Test
    void testAddition_Commutative_WithTargetUnit() {

        QuantityLength a =
                new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength b =
                new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength result1 = a.add(b, LengthUnit.YARDS);
        QuantityLength result2 = b.add(a, LengthUnit.YARDS);

        assertEquals(result1.getValue(),
                result2.getValue(),
                1e-6);
    }
    @Test
    void testAddition_NullTargetUnit() {

        assertThrows(IllegalArgumentException.class, () ->
                new QuantityLength(1.0, LengthUnit.FEET)
                        .add(new QuantityLength(12.0, LengthUnit.INCHES),
                                null));
    }
    @Test
    void givenGram_WhenConvertedToKilogram_ShouldMatch() {

        QuantityWeight weight =
                new QuantityWeight(1000.0, WeightUnit.GRAM);

        QuantityWeight result =
                weight.convertTo(WeightUnit.KILOGRAM);

        assertEquals(1.0, result.getValue(), 1e-6);
    }
    @Test
    void givenDifferentWeightUnits_WhenEqual_ShouldReturnTrue() {

        QuantityWeight w1 =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        QuantityWeight w2 =
                new QuantityWeight(1000.0, WeightUnit.GRAM);

        assertTrue(w1.equals(w2));
    }
    @Test
    void givenTwoWeights_WhenAdded_ShouldReturnCorrectResult() {

        QuantityWeight w1 =
                new QuantityWeight(1.0, WeightUnit.KILOGRAM);

        QuantityWeight w2 =
                new QuantityWeight(500.0, WeightUnit.GRAM);

        QuantityWeight result =
                w1.add(w2, WeightUnit.KILOGRAM);

        assertEquals(1.5, result.getValue(), 1e-6);
    }
    @Test
    void givenTwoLengths_WhenSubtracted_ShouldReturnCorrectResult() {

        Quantity<LengthUnit> l1 =
                new Quantity<>(5.0, LengthUnit.FEET);

        Quantity<LengthUnit> l2 =
                new Quantity<>(24.0, LengthUnit.INCHES);

        Quantity<LengthUnit> result = l1.subtract(l2);

        assertEquals(3.0, result.getValue());
    }
    @Test
    void givenLengths_WhenSubtractedWithTargetUnit_ShouldReturnCorrectUnit() {

        Quantity<LengthUnit> l1 =
                new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> l2 =
                new Quantity<>(6.0, LengthUnit.INCHES);

        Quantity<LengthUnit> result =
                l1.subtract(l2, LengthUnit.INCHES);

        assertEquals(6.0, result.getValue());
    }
    @Test
    void givenTwoLengths_WhenDivided_ShouldReturnRatio() {

        Quantity<LengthUnit> l1 =
                new Quantity<>(2.0, LengthUnit.FEET);

        Quantity<LengthUnit> l2 =
                new Quantity<>(12.0, LengthUnit.INCHES);

        double ratio = l1.divide(l2);

        assertEquals(2.0, ratio);
    }
    @Test
    void givenZeroQuantity_WhenDividing_ShouldThrowException() {

        Quantity<LengthUnit> l1 =
                new Quantity<>(2.0, LengthUnit.FEET);

        Quantity<LengthUnit> zero =
                new Quantity<>(0.0, LengthUnit.INCHES);

        assertThrows(ArithmeticException.class,
                () -> l1.divide(zero));
    }
}