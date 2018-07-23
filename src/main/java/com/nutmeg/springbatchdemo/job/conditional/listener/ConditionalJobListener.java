package com.nutmeg.springbatchdemo.job.conditional.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

@Named
@Slf4j
public class ConditionalJobListener extends JobExecutionListenerSupport {
    @Override
    public void afterJob(JobExecution jobExecution) {
        super.afterJob(jobExecution);

        if (!jobExecution.getAllFailureExceptions().isEmpty()) {
            log.info("*********** ATTENTION - JOB {} FAILED. CAUSE: {}",
                    jobExecution.getJobInstance().getJobName(),
                    exceptionsToString(jobExecution.getAllFailureExceptions()));
            jobExecution.setStatus(BatchStatus.FAILED);
        }
    }

    private String exceptionsToString(List<Throwable> list) {
        return list.stream()
                .map(t -> t.getMessage())
                .collect(Collectors.joining(", "));
    }
}
