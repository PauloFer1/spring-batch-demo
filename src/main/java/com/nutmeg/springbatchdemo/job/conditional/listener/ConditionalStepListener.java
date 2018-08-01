package com.nutmeg.springbatchdemo.job.conditional.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;

import javax.inject.Named;

@Slf4j
@Named
public class ConditionalStepListener extends StepExecutionListenerSupport {
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (BatchStatus.COMPLETED != stepExecution.getStatus()) {
            log.error("Something went wrong... Step[{}] is {}", stepExecution.getStepName(), stepExecution.getStatus());
        }
        return super.afterStep(stepExecution);
    }
}
