package com.nutmeg.springbatchdemo.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class Price {
    private String uuid;
    private LocalDateTime updatedAt;
    private BigDecimal price;
    private boolean isValid;
    private String stockUuid;
}
