package com.app.quantitymeasurement.services;

import com.app.quantitymeasurement.model.QuantityInputDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;

import java.util.List;

public interface IQuantityMeasurementService {

    QuantityMeasurementDTO compareQuantities(QuantityInputDTO input);

    QuantityMeasurementDTO convertQuantity(QuantityInputDTO input);

    QuantityMeasurementDTO addQuantities(QuantityInputDTO input);

    QuantityMeasurementDTO subtractQuantities(QuantityInputDTO input);

    QuantityMeasurementDTO multiplyQuantities(QuantityInputDTO input);

    QuantityMeasurementDTO divideQuantities(QuantityInputDTO input);

    List<QuantityMeasurementDTO> getHistoryByOperation(String operation);

    List<QuantityMeasurementDTO> getHistoryByMeasurementType(String measurementType);

    List<QuantityMeasurementDTO> getErrorHistory();

    long getOperationCount(String operation);
}
