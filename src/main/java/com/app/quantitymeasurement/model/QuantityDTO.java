package com.app.quantitymeasurement.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class QuantityDTO {

    @NotNull(message = "Value must not be null")
    private Double value;

    @NotEmpty(message = "Unit must not be empty")
    private String unit;

    @NotEmpty(message = "Measurement type must not be empty")
    @Pattern(
        regexp = "LengthUnit|WeightUnit|VolumeUnit|TemperatureUnit",
        message = "Measurement type must be LengthUnit, WeightUnit, VolumeUnit, or TemperatureUnit"
    )
    private String measurementType;
}
