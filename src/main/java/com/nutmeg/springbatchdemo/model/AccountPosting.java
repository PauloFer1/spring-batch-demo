package com.nutmeg.springbatchdemo.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountPosting {
    private String type;
    private BigDecimal value;
    private String signedType;
}
