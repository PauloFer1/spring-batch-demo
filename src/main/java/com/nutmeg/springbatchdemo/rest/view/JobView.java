package com.nutmeg.springbatchdemo.rest.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.batch.core.JobParameters;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobView {
    private Long jobReference;
    private Long jobInstance;
    private String jobName;
    private JobParameters jobParameters;
}
