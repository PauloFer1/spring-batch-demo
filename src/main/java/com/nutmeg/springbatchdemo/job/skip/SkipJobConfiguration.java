package com.nutmeg.springbatchdemo.job.skip;

import com.nutmeg.springbatchdemo.job.skip.exception.SkipJobException;
import com.nutmeg.springbatchdemo.job.skip.exception.SkipJobTimeoutException;
import com.nutmeg.springbatchdemo.job.skip.listener.SkipJobListener;
import com.nutmeg.springbatchdemo.job.skip.processor.SkipJobItemProcessor;
import com.nutmeg.springbatchdemo.job.skip.reader.SkipJobItemReader;
import com.nutmeg.springbatchdemo.job.skip.writer.SkipJobItemWriter;
import com.nutmeg.springbatchdemo.model.Price;
import com.nutmeg.springbatchdemo.model.Stock;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import java.util.List;

@Configuration
public class SkipJobConfiguration {

    private static final String SKIP_JOB_NAME = "skip_job";
    private static final int CHUNK_SIZE = 8;

    @Bean
    public Step skipJobStep(
            TaskExecutor taskExecutor,
            StepBuilderFactory stepBuilderFactory,
            SkipJobItemReader skipJobItemReader,
            SkipJobItemProcessor skipJobItemProcessor,
            SkipJobItemWriter skipJobItemWriter,
            SkipJobListener skipJobListener
    ) {
        return stepBuilderFactory.get(SKIP_JOB_NAME + "_step")
                .<Stock, List<Price>>chunk(CHUNK_SIZE)
                .reader(skipJobItemReader)
                .processor(skipJobItemProcessor)
                .writer(skipJobItemWriter)
                .faultTolerant()
                .skipLimit(10)
                .skip(SkipJobException.class)
                .retryLimit(4)
                .retry(SkipJobTimeoutException.class)
                .listener(skipJobListener)
                .taskExecutor(taskExecutor)
                .build();

    }

    /**
     * Gets all STOCKS and add some dummy PRICE data
     * @param jobBuilderFactory
     * @param skipJobStep
     * @return Job
     */
    @Bean(name = SKIP_JOB_NAME)
    public Job skipJob(
            JobBuilderFactory jobBuilderFactory,
            Step skipJobStep
    ) {
        return jobBuilderFactory.get(SKIP_JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .flow(skipJobStep)
                .end()
                .build();
    }
}
