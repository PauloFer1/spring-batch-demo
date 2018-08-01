package com.nutmeg.springbatchdemo.job.skip.exception;

public class SkipJobTimeoutException extends SkipJobException {
    public SkipJobTimeoutException(String msg) {
        super(msg);
    }
}
