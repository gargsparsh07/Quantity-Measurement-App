package com.bridgelabz.service;

import com.bridgelabz.model.QuantityDTO;

/**
 * UC15: IQuantityMeasurementService - Service Interface
 * Defines contract for all quantity measurement operations.
 * Accepts QuantityDTO input and returns QuantityDTO output.
 */
public interface IQuantityMeasurementService {

    /**
     * Compares two quantities for equality.
     * Returns QuantityDTO with value 1.0 (equal) or 0.0 (not equal).
     */
    QuantityDTO compare(QuantityDTO q1, QuantityDTO q2);

    /**
     * Converts a quantity to the target unit.
     */
    QuantityDTO convert(QuantityDTO source, QuantityDTO targetUnit);

    /**
     * Adds two quantities.
     * Result is in the unit of the first operand.
     */
    QuantityDTO add(QuantityDTO q1, QuantityDTO q2);

    /**
     * Subtracts q2 from q1.
     * Result is in the unit of the first operand.
     */
    QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2);

    /**
     * Divides q1 by q2.
     * Returns dimensionless scalar ratio.
     */
    QuantityDTO divide(QuantityDTO q1, QuantityDTO q2);
}