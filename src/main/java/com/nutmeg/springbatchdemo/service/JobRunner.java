package com.nutmeg.springbatchdemo.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

import javax.inject.Named;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.Date;

@Named
@AllArgsConstructor
public class JobRunner {

    private final ApplicationContext applicationContext;
    private final JobLauncher jobLauncher;

    public JobExecution runJob(String jobName, JsonNode parameters) throws JobExecutionException {
        Job job = lookupJob(jobName);
        return jobLauncher.run(job, buildJobParameters(parameters));
    }

    private JobParameters buildJobParameters(JsonNode parameters) {
        JobParametersBuilder builder = new JobParametersBuilder();
        parameters.fields().forEachRemaining(entry -> {
            JsonNode node = entry.getValue();
            if (node.isIntegralNumber()) {
                builder.addLong(entry.getKey(), node.asLong());
            } else if (node.isFloatingPointNumber()) {
                builder.addDouble(entry.getKey(), node.asDouble());
            } else {
                try {
                    Date date = Date.from(
                            LocalDate.parse(entry.getValue().asText())
                                    .atStartOfDay()
                                    .toInstant(ZoneOffset.UTC));
                    builder.addDate(entry.getKey(), date);
                } catch (DateTimeParseException e) {
                    builder.addString(entry.getKey(), entry.getValue().asText());
                }
            }
        });
        return builder.toJobParameters();
    }

    private Job lookupJob(String jobName) throws JobExecutionException {
        try {
            return applicationContext.getBean(jobName, Job.class);
        } catch (NoSuchBeanDefinitionException e) {
            throw new JobParametersInvalidException(
                    String.format("Unable to locate job named %s", jobName));
        } catch (BeanNotOfRequiredTypeException e) {
            throw new JobParametersInvalidException(
                    String.format("Bean named %s is not a Spring Batch Job", jobName));
        } catch (BeansException e) {
            throw new JobExecutionException(String.format("Unable to launch job %s", jobName), e);
        }
    }
}
