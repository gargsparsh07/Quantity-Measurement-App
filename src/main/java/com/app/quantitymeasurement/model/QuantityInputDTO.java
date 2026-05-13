package com.app.quantitymeasurement.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuantityInputDTO {

    @NotNull(message = "thisQuantityDTO must not be null")
    @Valid
    private QuantityDTO thisQuantityDTO;

    @NotNull(message = "thatQuantityDTO must not be null")
    @Valid
    private QuantityDTO thatQuantityDTO;

    private String targetUnit;
}
