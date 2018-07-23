package com.nutmeg.springbatchdemo.job.conditional;

import com.nutmeg.springbatchdemo.job.conditional.listener.ConditionalJobListener;
import com.nutmeg.springbatchdemo.job.conditional.processor.ConditionalJobItemProcessor;
import com.nutmeg.springbatchdemo.job.conditional.processor.MonthlyFeesJobItemProcessor;
import com.nutmeg.springbatchdemo.job.conditional.processor.RollBackJobItemProcessor;
import com.nutmeg.springbatchdemo.job.conditional.reader.ConditionalJobItemReader;
import com.nutmeg.springbatchdemo.job.conditional.writer.ConditionalJobItemWriter;
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
public class ConditionalJobConfiguration {

    private static final String JOB_NAME = "conditional_job";
    private static final int CHUNK_SIZE = 2;

    @Bean
    public Step invalidateDemo(
            TaskExecutor taskExecutor,
            StepBuilderFactory stepBuilderFactory,
            ConditionalJobItemReader conditionalJobItemReader,
            ConditionalJobItemProcessor conditionalJobItemProcessor,
            ConditionalJobItemWriter conditionalJobItemWriter
    ) {
        return stepBuilderFactory.get(JOB_NAME + "_invalidateDemo")
                .<Demo, Demo>chunk(CHUNK_SIZE)
                .reader(conditionalJobItemReader)
                .processor(conditionalJobItemProcessor)
                .writer(conditionalJobItemWriter)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public Step rollBackInvalidateDemo(
            TaskExecutor taskExecutor,
            StepBuilderFactory stepBuilderFactory,
            ConditionalJobItemReader conditionalJobItemReader,
            RollBackJobItemProcessor rollBackJobItemProcessor,
            ConditionalJobItemWriter conditionalJobItemWriter
    ) {
        return stepBuilderFactory.get(JOB_NAME + "_rollBackInvalidateDemo")
                .<Demo, Demo>chunk(CHUNK_SIZE)
                .reader(conditionalJobItemReader)
                .processor(rollBackJobItemProcessor)
                .writer(conditionalJobItemWriter)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public Step applyMonthlyFees(
            TaskExecutor taskExecutor,
            StepBuilderFactory stepBuilderFactory,
            SimpleJobItemReader simpleJobItemReader,
            MonthlyFeesJobItemProcessor monthlyFeesJobItemProcessor,
            SimpleJobItemWriter simpleJobItemWriter
    ) {
        return stepBuilderFactory.get(JOB_NAME + "_applyMonthlyFees")
                .<Demo, Demo>chunk(CHUNK_SIZE)
                .reader(simpleJobItemReader)
                .processor(monthlyFeesJobItemProcessor)
                .writer(simpleJobItemWriter)
                .taskExecutor(taskExecutor)
                .throttleLimit(8)
                .build();
    }

    @Bean(name = JOB_NAME)
    public Job conditionalJob(
            JobBuilderFactory jobBuilderFactory,
            Step invalidateDemo,
            Step applyMonthlyFees,
            Step rollBackInvalidateDemo,
            ConditionalJobListener conditionalJobListener
    ) {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .listener(conditionalJobListener)
                .start(invalidateDemo)
                .on("COMPLETED").to(applyMonthlyFees)
                .from(invalidateDemo).on("FAILED").to(rollBackInvalidateDemo)
                .end()
                .build();
    }

}
