package com.nutmeg.springbatchdemo.job.conditional.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.core.repository.JobRepository;

import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

@Named
@Slf4j
@RequiredArgsConstructor
public class ConditionalJobListener extends JobExecutionListenerSupport {

    private final JobRepository jobRepository;

    @Override
    public void afterJob(JobExecution jobExecution) {
        super.afterJob(jobExecution);

        if (!jobExecution.getAllFailureExceptions().isEmpty()) {
            log.info("*********** ATTENTION - JOB {} FAILED. CAUSE: {}",
                    jobExecution.getJobInstance().getJobName(),
                    exceptionsToString(jobExecution.getAllFailureExceptions()));
            jobExecution.getStepExecutions().stream()
                    .filter(stepExecution -> stepExecution.getStatus().equals(BatchStatus.ABANDONED))
                    .forEach(stepExecution -> {
                        stepExecution.setStatus(BatchStatus.COMPLETED);
                        jobRepository.update(stepExecution);
                    });
            jobExecution.setStatus(BatchStatus.FAILED);
        }
    }

    private String exceptionsToString(List<Throwable> list) {
        return list.stream()
                .map(t -> t.getMessage())
                .collect(Collectors.joining(", "));
    }
}
