package com.nutmeg.springbatchdemo.job.perioddetail;

import com.nutmeg.springbatchdemo.job.perioddetail.processor.PeriodDetailItemProcessor;
import com.nutmeg.springbatchdemo.job.perioddetail.reader.PeriodDetailItemReader;
import com.nutmeg.springbatchdemo.job.perioddetail.writer.PeriodDetailItemWriter;
import com.nutmeg.springbatchdemo.model.PeriodDetail;
import com.nutmeg.springbatchdemo.model.WebFund;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class PeriodDetailJobConfiguration {

    private static final String JOB_NAME = "period_detail";
    private static final int CHUNK_SIZE = 100;

    @Bean
    public Step getUserModel(
        TaskExecutor taskExecutor,
        StepBuilderFactory stepBuilderFactory,
        PeriodDetailItemReader periodDetailItemReader,
        PeriodDetailItemProcessor periodDetailItemProcessor,
        PeriodDetailItemWriter periodDetailItemWriter)
    {
        return stepBuilderFactory.get(JOB_NAME + "_step")
            .<PeriodDetail, WebFund>chunk(CHUNK_SIZE)
            .reader(periodDetailItemReader)
            .processor(periodDetailItemProcessor)
            .writer(periodDetailItemWriter)
            .taskExecutor(taskExecutor)
            .build();
    }

    @Bean(name = JOB_NAME)
    public Job findModelForFund(
        JobBuilderFactory jobBuilderFactory,
        Step getUserModel
    ) {
            return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .flow(getUserModel)
                .end()
                .build();
    }
}
