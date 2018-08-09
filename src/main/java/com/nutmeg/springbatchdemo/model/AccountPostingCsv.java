package com.nutmeg.springbatchdemo.model;

import lombok.Data;

@Data
public class AccountPostingCsv {
    private String date;
    private String fundUuidOwn;
    private String fundName;
    private String returnIndex;
    private String medianIndex;
    private String absoluteError;
    private String expectedReturn;
    private String absoluteErrorAgainstExp;
    private String dividendAccrualBfwd;
    private String dividendAccrualCfwd;
    private String contributions;
    private String promotions;
    private String difference;
}

