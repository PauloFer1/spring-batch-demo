package com.nutmeg.springbatchdemo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WebFund {
    private String uuid;
    private String currentModel;
    private String changeJson;
}
