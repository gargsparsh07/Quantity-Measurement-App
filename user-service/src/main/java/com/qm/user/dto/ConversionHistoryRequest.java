package com.qm.user.dto;
import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor
public class ConversionHistoryRequest {
    private String type;
    private String fromUnit;
    private String toUnit;
    private double inputValue;
    private double outputValue;
}
