package com.nutmeg.springbatchdemo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Stock {
    private String uuid;
    private String name;
}
