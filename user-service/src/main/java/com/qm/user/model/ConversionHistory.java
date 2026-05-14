package com.qm.user.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "conversion_history")
public class ConversionHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String type;
    private String fromUnit;
    private String toUnit;
    private double inputValue;
    private double outputValue;
    private LocalDateTime timestamp;

    @PrePersist protected void onCreate() {
        if (timestamp == null) timestamp = LocalDateTime.now();
    }
}
