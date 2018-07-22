package com.nutmeg.springbatchdemo.job.simple;

import com.nutmeg.springbatchdemo.job.simple.listener.SimpleJobListener;
import com.nutmeg.springbatchdemo.job.simple.processor.SimpleJobItemProcessor;
import com.nutmeg.springbatchdemo.job.simple.reader.SimpleJobItemReader;
import com.nutmeg.springbatchdemo.job.simple.writer.SimpleJobItemWriter;
import com.nutmeg.springbatchdemo.model.Demo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;


@Configuration
public class SimpleJobConfiguration {

    private static final String JOB_NAME = "simple_job";
    private static final int CHUNK_SIZE = 10;

    @Bean
    public Step simpleJobStep(
            TaskExecutor taskExecutor,
            StepBuilderFactory stepBuilderFactory,
            SimpleJobItemReader simpleJobItemReader,
            SimpleJobItemProcessor simpleJobItemProcessor,
            SimpleJobItemWriter simpleJobItemWriter
    ) {
        return stepBuilderFactory.get(JOB_NAME + "_step")
                .<Demo, Demo>chunk(CHUNK_SIZE)
                .reader(simpleJobItemReader)
                .processor(simpleJobItemProcessor)
                .writer(simpleJobItemWriter)
                .taskExecutor(taskExecutor)
                .throttleLimit(8)
                .build();
    }

    /**
     * Gets all data from DEMO table and update the PRICE (Add fee) and UPDATED_AT columns.
     * @param jobBuilderFactory
     * @param simpleJobStep
     * @return Job
     */
    @Bean(name = JOB_NAME)
    public Job simpleJob(
            JobBuilderFactory jobBuilderFactory,
            Step simpleJobStep,
            SimpleJobListener simpleJobListener
    ) {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .listener(simpleJobListener)
                .flow(simpleJobStep)
                .end()
                .build();
    }
}
