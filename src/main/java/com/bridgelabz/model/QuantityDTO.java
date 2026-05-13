package com.bridgelabz.model;

/**
 * UC15: QuantityDTO - Data Transfer Object (POJO)
 * Used for transferring quantity data between layers.
 */
public class QuantityDTO {

    // ======== Inner Interface ========
    public interface IMeasurableUnit {
        String getUnitName();
        String getMeasurementType();
    }

    // ======== Inner Enums ========
    public enum LengthUnit implements IMeasurableUnit {
        FEET, INCHES, YARDS, CENTIMETERS;
        @Override public String getUnitName()        { return name(); }
        @Override public String getMeasurementType() { return "LENGTH"; }
    }

    public enum WeightUnit implements IMeasurableUnit {
        KILOGRAM, GRAM, TON;
        @Override public String getUnitName()        { return name(); }
        @Override public String getMeasurementType() { return "WEIGHT"; }
    }

    public enum VolumeUnit implements IMeasurableUnit {
        LITRE, MILLILITRE, GALLON;
        @Override public String getUnitName()        { return name(); }
        @Override public String getMeasurementType() { return "VOLUME"; }
    }

    public enum TemperatureUnit implements IMeasurableUnit {
        CELSIUS, FAHRENHEIT, KELVIN;
        @Override public String getUnitName()        { return name(); }
        @Override public String getMeasurementType() { return "TEMPERATURE"; }
    }

    // ======== Fields ========
    private double value;
    private IMeasurableUnit unit;

    // ======== Constructors ========
    public QuantityDTO() {}

    public QuantityDTO(double value, IMeasurableUnit unit) {
        this.value = value;
        this.unit  = unit;
    }

    // ======== Getters & Setters ========
    public double          getValue()                    { return value; }
    public void            setValue(double value)        { this.value = value; }
    public IMeasurableUnit getUnit()                     { return unit; }
    public void            setUnit(IMeasurableUnit unit) { this.unit = unit; }

    @Override
    public String toString() {
        return "QuantityDTO{value=" + value
                + ", unit="  + (unit != null ? unit.getUnitName() : "null")
                + ", type="  + (unit != null ? unit.getMeasurementType() : "null")
                + "}";
    }
}