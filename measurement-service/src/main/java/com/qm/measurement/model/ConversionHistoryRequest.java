package com.qm.measurement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversionHistoryRequest {
    private String type;
    private String fromUnit;
    private String toUnit;
    private double inputValue;
    private double outputValue;
}
