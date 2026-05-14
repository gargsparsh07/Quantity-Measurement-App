package com.qm.measurement.model;

import lombok.Data;

@Data
public class ConversionHistoryResponse {
    private Long id;
    private Long userId;
    private String type;
    private String fromUnit;
    private String toUnit;
    private double inputValue;
    private double outputValue;
}
