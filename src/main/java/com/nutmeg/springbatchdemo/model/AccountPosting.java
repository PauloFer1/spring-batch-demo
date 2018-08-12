package com.nutmeg.springbatchdemo.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AccountPosting {
    private String uuid;
    private String date;
    private String custodianAccount;
    private String fundUuidOwn;
    private String owner;
    private String assetCode;
    private String description;
    private String price;
    private String realised;
    private String type;
    private String units;
    private String operationalStatus;
    private String internalAccount;
    private String wrapperType;
    private String reference;
    private String externalReference;
    private String internalReference;
    private String basisOfAllocation;
    private String source;
    private String taxYear;
    private String currency;
    private BigDecimal value;
}
