package com.app.quantitymeasurement.model;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class QuantityMeasurementDTO {

    private Double thisValue;
    private String thisUnit;
    private String thisMeasurementType;

    private Double thatValue;
    private String thatUnit;
    private String thatMeasurementType;

    private String operation;
    private String resultString;
    private Double resultValue;
    private String resultUnit;
    private String resultMeasurementType;
    private String errorMessage;
    private Boolean error = false;

    public static QuantityMeasurementDTO fromEntity(QuantityMeasurementEntity entity) {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setThisValue(entity.getThisValue());
        dto.setThisUnit(entity.getThisUnit());
        dto.setThisMeasurementType(entity.getThisMeasurementType());
        dto.setThatValue(entity.getThatValue());
        dto.setThatUnit(entity.getThatUnit());
        dto.setThatMeasurementType(entity.getThatMeasurementType());
        dto.setOperation(entity.getOperation());
        dto.setResultString(entity.getResultString());
        dto.setResultValue(entity.getResultValue());
        dto.setResultUnit(entity.getResultUnit());
        dto.setResultMeasurementType(entity.getResultMeasurementType());
        dto.setErrorMessage(entity.getErrorMessage());
        dto.setError(entity.getIsError());
        return dto;
    }

    public QuantityMeasurementEntity toEntity() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setThisValue(this.thisValue);
        entity.setThisUnit(this.thisUnit);
        entity.setThisMeasurementType(this.thisMeasurementType);
        entity.setThatValue(this.thatValue);
        entity.setThatUnit(this.thatUnit);
        entity.setThatMeasurementType(this.thatMeasurementType);
        entity.setOperation(this.operation);
        entity.setResultString(this.resultString);
        entity.setResultValue(this.resultValue);
        entity.setResultUnit(this.resultUnit);
        entity.setResultMeasurementType(this.resultMeasurementType);
        entity.setErrorMessage(this.errorMessage);
        entity.setIsError(this.error != null && this.error);
        return entity;
    }

    public static List<QuantityMeasurementDTO> fromEntityList(List<QuantityMeasurementEntity> entities) {
        return entities.stream()
            .map(QuantityMeasurementDTO::fromEntity)
            .collect(Collectors.toList());
    }

    public static List<QuantityMeasurementEntity> toEntityList(List<QuantityMeasurementDTO> dtos) {
        return dtos.stream()
            .map(QuantityMeasurementDTO::toEntity)
            .collect(Collectors.toList());
    }
}
