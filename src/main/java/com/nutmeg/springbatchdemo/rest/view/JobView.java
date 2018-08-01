package com.nutmeg.springbatchdemo.rest.view;

import lombok.Builder;
import lombok.Data;
import org.springframework.batch.core.JobParameters;

@Data
@Builder
public class JobView {
    private Long jobReference;
    private Long jobInstance;
    private String jobName;
    private JobParameters jobParameters;
}
