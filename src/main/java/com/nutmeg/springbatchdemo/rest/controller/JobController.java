package com.nutmeg.springbatchdemo.rest.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.nutmeg.springbatchdemo.rest.view.JobView;
import com.nutmeg.springbatchdemo.service.JobRunner;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@AllArgsConstructor
public class JobController {

    private final JobRunner jobRunner;

    @RequestMapping(value = "job/{jobName}", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity runJob(
            @RequestBody final JsonNode body,
            @PathVariable("jobName") final String jobName) throws Exception {

        log.info("Starting JOB: {}", jobName);

        JobExecution jobExecution = jobRunner.runJob(jobName, body);
        JobView jobView = JobView.builder()
                .jobName(jobName)
                .jobReference(jobExecution.getJobId())
                .jobInstance(jobExecution.getJobInstance().getInstanceId())
                .jobParameters(jobExecution.getJobParameters())
                .build();

        return ResponseEntity.ok(jobView);
    }
}
