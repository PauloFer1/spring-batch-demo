package com.nutmeg.springbatchdemo.exception;

public class AccountPostingNotFoundException extends Exception {

    private static final String MESSAGE = "Account posting with UUID: %s not found.";

    public AccountPostingNotFoundException(String uuid) {
        super(String.format(MESSAGE, uuid));
    }
}
