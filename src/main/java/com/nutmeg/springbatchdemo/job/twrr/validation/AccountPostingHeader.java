package com.nutmeg.springbatchdemo.job.twrr.validation;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AccountPostingHeader {
    DATE("date"),
    FUNDUUIDOWN("fundUuidOwn"),
    RETURNINDEX("returnIndex"),
    MEDIANINDEX("medianIndex"),
    ABSOLUTEERROR("absoluteError"),
    EXPECTEDRETURN("expectedReturn"),
    ABSOLUTEERRORAGAINSTEXP("absoluteErrorAgainstExp"),
    DIVIDENDACCRUALBFWD("dividendAccrualBfwd"),
    DIVIDENDACCRUALCFWD("dividendAccrualCfwd"),
    CONTRIBUTIONS("contributions"),
    PROMOTIONS("promotions"),
    DIFFERENCE("difference");

    private final String value;

    AccountPostingHeader(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static AccountPostingHeader fromSymbol(String symbol) {
        for (AccountPostingHeader accountPostingHeader : values()) {
            if (accountPostingHeader.value.equals(symbol)) {
                return accountPostingHeader;
            }
        }
        throw new IllegalArgumentException(String.format("%s is not a valid accountPostingHeader.", symbol));
    }
}