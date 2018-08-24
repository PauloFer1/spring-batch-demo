package com.nutmeg.springbatchdemo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TradedModel {
    private String fundUuid;
    private String modelName;
    private String createdOn;
}
