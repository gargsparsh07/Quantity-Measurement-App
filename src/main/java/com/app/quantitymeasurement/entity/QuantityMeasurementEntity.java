package com.app.quantitymeasurement.entity;

import java.sql.Timestamp;

public class QuantityMeasurementEntity {

    private Long id;
    private double firstQuantity;
    private String firstUnit;
    private double secondQuantity;
    private String secondUnit;
    private double result;
    private String operationType;
    private String measurementType;
    private Timestamp createdAt;

    public QuantityMeasurementEntity() {}

    public QuantityMeasurementEntity(double firstQuantity, String firstUnit,
                                     double secondQuantity, String secondUnit,
                                     double result, String operationType, String measurementType) {
        this.firstQuantity = firstQuantity;
        this.firstUnit = firstUnit;
        this.secondQuantity = secondQuantity;
        this.secondUnit = secondUnit;
        this.result = result;
        this.operationType = operationType;
        this.measurementType = measurementType;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public double getFirstQuantity() { return firstQuantity; }
    public void setFirstQuantity(double firstQuantity) { this.firstQuantity = firstQuantity; }
    public String getFirstUnit() { return firstUnit; }
    public void setFirstUnit(String firstUnit) { this.firstUnit = firstUnit; }
    public double getSecondQuantity() { return secondQuantity; }
    public void setSecondQuantity(double secondQuantity) { this.secondQuantity = secondQuantity; }
    public String getSecondUnit() { return secondUnit; }
    public void setSecondUnit(String secondUnit) { this.secondUnit = secondUnit; }
    public double getResult() { return result; }
    public void setResult(double result) { this.result = result; }
    public String getOperationType() { return operationType; }
    public void setOperationType(String operationType) { this.operationType = operationType; }
    public String getMeasurementType() { return measurementType; }
    public void setMeasurementType(String measurementType) { this.measurementType = measurementType; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return String.format("Entity[%s %.2f %s -> %.2f %s = %.4f]",
            measurementType, firstQuantity, firstUnit, secondQuantity, secondUnit, result);
    }
}
