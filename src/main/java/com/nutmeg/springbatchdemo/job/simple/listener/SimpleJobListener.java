package com.nutmeg.springbatchdemo.job.simple.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

import javax.inject.Named;

@Slf4j
@Named
public class SimpleJobListener extends JobExecutionListenerSupport {

    @Override
    public void afterJob(JobExecution jobExecution) {
        super.afterJob(jobExecution);

        log.info("Job {} complete with {} items in {} ms.",
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getStepExecutions().stream().filter(
                        stepExecution -> stepExecution.getStatus().equals(BatchStatus.COMPLETED) || stepExecution.getStatus().equals(BatchStatus.FAILED)
                ).findFirst().get().getWriteCount(),
                jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime());
    }
}
