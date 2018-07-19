package com.nutmeg.springbatchdemo.rest.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.nutmeg.springbatchdemo.runner.JobRunner;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@AllArgsConstructor
public class JobController {

    private final JobRunner jobRunner;

    public ResponseEntity runJob(
            @RequestBody final JsonNode body,
            @PathVariable("jobName") final String jobName) throws Exception {

        log.info("Starting JOB: {}", jobName);

        JobExecution jobExecution = jobRunner.runJob(jobName, body);
    }
}
