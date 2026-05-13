package com.bridgelabz.model;

import com.bridgelabz.IMeasurable;

/**
 * UC15: QuantityModel - Generic Internal POJO
 * Used within the service layer for performing operations.
 */
public class QuantityModel<U extends IMeasurable> {

    // ======== Fields ========
    private double value;
    private U unit;

    // ======== Constructors ========
    public QuantityModel() {}

    public QuantityModel(double value, U unit) {
        if (unit == null)
            throw new IllegalArgumentException("Unit cannot be null");
        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Value must be a finite number");

        this.value = value;
        this.unit  = unit;
    }

    // ======== Getters & Setters ========
    public double getValue()             { return value; }
    public void   setValue(double value) { this.value = value; }
    public U      getUnit()              { return unit; }
    public void   setUnit(U unit)        { this.unit = unit; }

    @Override
    public String toString() {
        return "QuantityModel{value=" + value
                + ", unit=" + (unit != null ? unit.getUnitName() : "null")
                + "}";
    }
}