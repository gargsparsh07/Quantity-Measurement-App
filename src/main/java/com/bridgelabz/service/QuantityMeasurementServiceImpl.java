package com.bridgelabz.service;

import com.bridgelabz.*;
import com.bridgelabz.exception.QuantityMeasurementException;
import com.bridgelabz.model.QuantityDTO;
import com.bridgelabz.model.QuantityMeasurementEntity;
import com.bridgelabz.repository.IQuantityMeasurementRepository;

/**
 * UC15: QuantityMeasurementServiceImpl - Service Layer
 * Contains all core business logic for quantity operations.
 */
public class QuantityMeasurementServiceImpl
        implements IQuantityMeasurementService {

    // ======== Dependency ========
    private final IQuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(
            IQuantityMeasurementRepository repository) {
        if (repository == null)
            throw new IllegalArgumentException("Repository cannot be null");
        this.repository = repository;
    }

    // ======== compare ========
    @Override
    public QuantityDTO compare(QuantityDTO q1, QuantityDTO q2) {
        validateInput(q1, "q1");
        validateInput(q2, "q2");
        try {
            validateSameCategory(q1, q2);
            Quantity<?> qty1  = toQuantity(q1);
            Quantity<?> qty2  = toQuantity(q2);
            boolean     equal = compareUnchecked(qty1, qty2);
            QuantityDTO result = new QuantityDTO(equal ? 1.0 : 0.0, null);
            repository.save(
                    new QuantityMeasurementEntity("COMPARE", q1, q2, result));
            return result;
        } catch (QuantityMeasurementException e) {
            repository.save(
                    new QuantityMeasurementEntity("COMPARE", q1, q2,
                            e.getMessage()));
            throw e;
        } catch (Exception e) {
            repository.save(
                    new QuantityMeasurementEntity("COMPARE", q1, q2,
                            e.getMessage()));
            throw new QuantityMeasurementException(
                    "Compare failed: " + e.getMessage(), e);
        }
    }

    // ======== convert ========
    @Override
    public QuantityDTO convert(QuantityDTO source, QuantityDTO targetUnit) {
        validateInput(source, "source");
        validateInput(targetUnit, "targetUnit");
        try {
            validateSameCategory(source, targetUnit);
            Quantity<?> qty       = toQuantity(source);
            IMeasurable target    = resolveUnit(targetUnit);
            Quantity<?> converted = convertUnchecked(qty, target);
            QuantityDTO result    =
                    new QuantityDTO(converted.getValue(), targetUnit.getUnit());
            repository.save(
                    new QuantityMeasurementEntity("CONVERT", source, result));
            return result;
        } catch (QuantityMeasurementException e) {
            repository.save(
                    new QuantityMeasurementEntity("CONVERT", source,
                            targetUnit, e.getMessage()));
            throw e;
        } catch (Exception e) {
            repository.save(
                    new QuantityMeasurementEntity("CONVERT", source,
                            targetUnit, e.getMessage()));
            throw new QuantityMeasurementException(
                    "Convert failed: " + e.getMessage(), e);
        }
    }

    // ======== add ========
    @Override
    public QuantityDTO add(QuantityDTO q1, QuantityDTO q2) {
        validateInput(q1, "q1");
        validateInput(q2, "q2");
        try {
            validateSameCategory(q1, q2);
            Quantity<?> qty1   = toQuantity(q1);
            Quantity<?> qty2   = toQuantity(q2);
            Quantity<?> sum    = addUnchecked(qty1, qty2);
            QuantityDTO result = new QuantityDTO(sum.getValue(), q1.getUnit());
            repository.save(
                    new QuantityMeasurementEntity("ADD", q1, q2, result));
            return result;
        } catch (QuantityMeasurementException e) {
            repository.save(
                    new QuantityMeasurementEntity("ADD", q1, q2,
                            e.getMessage()));
            throw e;
        } catch (UnsupportedOperationException e) {
            repository.save(
                    new QuantityMeasurementEntity("ADD", q1, q2,
                            e.getMessage()));
            throw new QuantityMeasurementException(
                    "Add not supported: " + e.getMessage(), e);
        } catch (Exception e) {
            repository.save(
                    new QuantityMeasurementEntity("ADD", q1, q2,
                            e.getMessage()));
            throw new QuantityMeasurementException(
                    "Add failed: " + e.getMessage(), e);
        }
    }

    // ======== subtract ========
    @Override
    public QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2) {
        validateInput(q1, "q1");
        validateInput(q2, "q2");
        try {
            validateSameCategory(q1, q2);
            Quantity<?> qty1       = toQuantity(q1);
            Quantity<?> qty2       = toQuantity(q2);
            Quantity<?> difference = subtractUnchecked(qty1, qty2);
            QuantityDTO result     =
                    new QuantityDTO(difference.getValue(), q1.getUnit());
            repository.save(
                    new QuantityMeasurementEntity("SUBTRACT", q1, q2, result));
            return result;
        } catch (QuantityMeasurementException e) {
            repository.save(
                    new QuantityMeasurementEntity("SUBTRACT", q1, q2,
                            e.getMessage()));
            throw e;
        } catch (Exception e) {
            repository.save(
                    new QuantityMeasurementEntity("SUBTRACT", q1, q2,
                            e.getMessage()));
            throw new QuantityMeasurementException(
                    "Subtract failed: " + e.getMessage(), e);
        }
    }

    // ======== divide ========
    @Override
    public QuantityDTO divide(QuantityDTO q1, QuantityDTO q2) {
        validateInput(q1, "q1");
        validateInput(q2, "q2");
        try {
            validateSameCategory(q1, q2);
            Quantity<?> qty1  = toQuantity(q1);
            Quantity<?> qty2  = toQuantity(q2);
            double      ratio = divideUnchecked(qty1, qty2);
            QuantityDTO result = new QuantityDTO(ratio, null);
            repository.save(
                    new QuantityMeasurementEntity("DIVIDE", q1, q2, result));
            return result;
        } catch (ArithmeticException e) {
            repository.save(
                    new QuantityMeasurementEntity("DIVIDE", q1, q2,
                            e.getMessage()));
            throw new QuantityMeasurementException("Division by zero", e);
        } catch (QuantityMeasurementException e) {
            repository.save(
                    new QuantityMeasurementEntity("DIVIDE", q1, q2,
                            e.getMessage()));
            throw e;
        } catch (Exception e) {
            repository.save(
                    new QuantityMeasurementEntity("DIVIDE", q1, q2,
                            e.getMessage()));
            throw new QuantityMeasurementException(
                    "Divide failed: " + e.getMessage(), e);
        }
    }

    // ======== Private Helpers: DTO → Quantity ========
    @SuppressWarnings("unchecked")
    private <U extends IMeasurable> Quantity<U> toQuantity(QuantityDTO dto) {
        IMeasurable unit = resolveUnit(dto);
        return new Quantity<>(dto.getValue(), (U) unit);
    }

    private IMeasurable resolveUnit(QuantityDTO dto) {
        if (dto.getUnit() == null)
            throw new QuantityMeasurementException(
                    "Unit cannot be null in DTO");
        String type     = dto.getUnit().getMeasurementType();
        String unitName = dto.getUnit().getUnitName();
        return switch (type) {
            case "LENGTH"      -> LengthUnit.valueOf(unitName);
            case "WEIGHT"      -> WeightUnit.valueOf(unitName);
            case "VOLUME"      -> VolumeUnit.valueOf(unitName);
            case "TEMPERATURE" -> TemperatureUnit.valueOf(unitName);
            default -> throw new QuantityMeasurementException(
                    "Unknown measurement type: " + type);
        };
    }

    // ======== Unchecked Operation Wrappers ========
    @SuppressWarnings({"rawtypes", "unchecked"})
    private boolean compareUnchecked(Quantity<?> q1, Quantity<?> q2) {
        return ((Quantity) q1).equals(q2);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Quantity<?> convertUnchecked(Quantity<?> qty, IMeasurable target) {
        return ((Quantity) qty).convertTo(target);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Quantity<?> addUnchecked(Quantity<?> q1, Quantity<?> q2) {
        return ((Quantity) q1).add(q2);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Quantity<?> subtractUnchecked(Quantity<?> q1, Quantity<?> q2) {
        return ((Quantity) q1).subtract(q2);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private double divideUnchecked(Quantity<?> q1, Quantity<?> q2) {
        return ((Quantity) q1).divide(q2);
    }

    // ======== Validation ========
    private void validateInput(QuantityDTO dto, String name) {
        if (dto == null)
            throw new QuantityMeasurementException(name + " cannot be null");
    }

    private void validateSameCategory(QuantityDTO q1, QuantityDTO q2) {
        if (q1.getUnit() == null || q2.getUnit() == null) return;
        String type1 = q1.getUnit().getMeasurementType();
        String type2 = q2.getUnit().getMeasurementType();
        if (!type1.equals(type2))
            throw new QuantityMeasurementException(
                    "Cross-category operation not allowed: "
                            + type1 + " vs " + type2);
    }
}