package com.app.quantitymeasurement.services;

import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.model.*;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import com.app.quantitymeasurement.unit.IMeasurable;
import com.app.quantitymeasurement.unit.LengthUnit;
import com.app.quantitymeasurement.unit.TemperatureUnit;
import com.app.quantitymeasurement.unit.VolumeUnit;
import com.app.quantitymeasurement.unit.WeightUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementServiceImpl.class);

    @Autowired
    private QuantityMeasurementRepository repository;

    @Override
    public QuantityMeasurementDTO compareQuantities(QuantityInputDTO input) {
        QuantityMeasurementDTO dto = buildBaseDTO(input, "compare");
        try {
            QuantityModel<IMeasurable> thisModel = convertDtoToModel(input.getThisQuantityDTO());
            QuantityModel<IMeasurable> thatModel = convertDtoToModel(input.getThatQuantityDTO());
            boolean result = thisModel.equals(thatModel);
            dto.setResultString(String.valueOf(result));
            dto.setResultValue(0.0);
        } catch (Exception e) {
            setError(dto, e.getMessage());
        }
        repository.save(dto.toEntity());
        return dto;
    }

    @Override
    public QuantityMeasurementDTO convertQuantity(QuantityInputDTO input) {
        QuantityMeasurementDTO dto = buildBaseDTO(input, "convert");
        try {
            QuantityModel<IMeasurable> thisModel = convertDtoToModel(input.getThisQuantityDTO());
            IMeasurable targetUnit = resolveUnit(input.getThatQuantityDTO().getUnit(),
                input.getThatQuantityDTO().getMeasurementType());
            double result = thisModel.convertTo(targetUnit);
            dto.setResultValue(result);
        } catch (Exception e) {
            setError(dto, e.getMessage());
        }
        repository.save(dto.toEntity());
        return dto;
    }

    @Override
    public QuantityMeasurementDTO addQuantities(QuantityInputDTO input) {
        return performArithmetic(input, "add");
    }

    @Override
    public QuantityMeasurementDTO subtractQuantities(QuantityInputDTO input) {
        return performArithmetic(input, "subtract");
    }

    @Override
    public QuantityMeasurementDTO multiplyQuantities(QuantityInputDTO input) {
        return performArithmetic(input, "multiply");
    }

    @Override
    public QuantityMeasurementDTO divideQuantities(QuantityInputDTO input) {
        return performArithmetic(input, "divide");
    }

    private QuantityMeasurementDTO performArithmetic(QuantityInputDTO input, String operation) {
        QuantityMeasurementDTO dto = buildBaseDTO(input, operation);
        try {
            QuantityModel<IMeasurable> thisModel = convertDtoToModel(input.getThisQuantityDTO());
            QuantityModel<IMeasurable> thatModel = convertDtoToModel(input.getThatQuantityDTO());

            if (!input.getThisQuantityDTO().getMeasurementType()
                    .equals(input.getThatQuantityDTO().getMeasurementType())) {
                throw new QuantityMeasurementException(operation + " Error: Cannot perform arithmetic " +
                    "between different measurement categories: " +
                    input.getThisQuantityDTO().getMeasurementType() + " and " +
                    input.getThatQuantityDTO().getMeasurementType());
            }

            String targetUnitName = input.getTargetUnit() != null
                ? input.getTargetUnit()
                : input.getThisQuantityDTO().getUnit();
            IMeasurable targetUnit = resolveUnit(targetUnitName, input.getThisQuantityDTO().getMeasurementType());

            double result;
            switch (operation) {
                case "add":      result = thisModel.add(thatModel, targetUnit); break;
                case "subtract": result = thisModel.subtract(thatModel, targetUnit); break;
                case "multiply": result = thisModel.multiply(thatModel, targetUnit); break;
                case "divide":   result = thisModel.divide(thatModel, targetUnit); break;
                default: throw new QuantityMeasurementException("Unknown operation: " + operation);
            }

            dto.setResultValue(result);
            dto.setResultUnit(targetUnitName);
            dto.setResultMeasurementType(input.getThisQuantityDTO().getMeasurementType());
        } catch (Exception e) {
            setError(dto, e.getMessage());
        }
        repository.save(dto.toEntity());
        return dto;
    }

    @Override
    public List<QuantityMeasurementDTO> getHistoryByOperation(String operation) {
        return QuantityMeasurementDTO.fromEntityList(repository.findByOperation(operation.toUpperCase()));
    }

    @Override
    public List<QuantityMeasurementDTO> getHistoryByMeasurementType(String measurementType) {
        return QuantityMeasurementDTO.fromEntityList(repository.findByThisMeasurementType(measurementType));
    }

    @Override
    public List<QuantityMeasurementDTO> getErrorHistory() {
        return QuantityMeasurementDTO.fromEntityList(repository.findByIsErrorTrue());
    }

    @Override
    public long getOperationCount(String operation) {
        return repository.countByOperationAndIsErrorFalse(operation.toUpperCase());
    }

    private QuantityMeasurementDTO buildBaseDTO(QuantityInputDTO input, String operation) {
        QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
        dto.setThisValue(input.getThisQuantityDTO().getValue());
        dto.setThisUnit(input.getThisQuantityDTO().getUnit());
        dto.setThisMeasurementType(input.getThisQuantityDTO().getMeasurementType());
        dto.setThatValue(input.getThatQuantityDTO().getValue());
        dto.setThatUnit(input.getThatQuantityDTO().getUnit());
        dto.setThatMeasurementType(input.getThatQuantityDTO().getMeasurementType());
        dto.setOperation(operation);
        dto.setError(false);
        return dto;
    }

    private void setError(QuantityMeasurementDTO dto, String message) {
        dto.setError(true);
        dto.setErrorMessage(message);
        logger.error("Operation error: {}", message);
    }

    private QuantityModel<IMeasurable> convertDtoToModel(QuantityDTO dto) {
        IMeasurable unit = resolveUnit(dto.getUnit(), dto.getMeasurementType());
        return new QuantityModel<>(dto.getValue(), unit);
    }

    private IMeasurable resolveUnit(String unitName, String measurementType) {
        try {
            switch (measurementType) {
                case "LengthUnit":      return LengthUnit.valueOf(unitName);
                case "WeightUnit":      return WeightUnit.valueOf(unitName);
                case "VolumeUnit":      return VolumeUnit.valueOf(unitName);
                case "TemperatureUnit": return TemperatureUnit.valueOf(unitName);
                default: throw new QuantityMeasurementException("Unknown measurement type: " + measurementType);
            }
        } catch (IllegalArgumentException e) {
            throw new QuantityMeasurementException("Unit must be valid for the specified measurement type");
        }
    }
}
