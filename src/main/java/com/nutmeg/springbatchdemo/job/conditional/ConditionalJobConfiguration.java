package com.nutmeg.springbatchdemo.job.conditional;

import com.nutmeg.springbatchdemo.job.conditional.listener.ConditionalJobListener;
import com.nutmeg.springbatchdemo.job.conditional.listener.ConditionalStepListener;
import com.nutmeg.springbatchdemo.job.conditional.processor.ConditionalJobItemProcessor;
import com.nutmeg.springbatchdemo.job.conditional.processor.MonthlyFeesJobItemProcessor;
import com.nutmeg.springbatchdemo.job.conditional.processor.RollBackJobItemProcessor;
import com.nutmeg.springbatchdemo.job.conditional.reader.ConditionalJobItemReader;
import com.nutmeg.springbatchdemo.job.conditional.writer.ConditionalJobItemWriter;
import com.nutmeg.springbatchdemo.job.simple.reader.SimpleJobItemReader;
import com.nutmeg.springbatchdemo.job.simple.writer.SimpleJobItemWriter;
import com.nutmeg.springbatchdemo.model.Price;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ConditionalJobConfiguration {

    private static final String CONDITIONAL_JOB_NAME = "conditional_job";
    private static final String FAILED_JOB_NAME = "failed_job";
    private static final String STOP_STEP_JOB_NAME = "stop_step_job";
    private static final int CHUNK_SIZE = 6;

    @Bean
    public Step invalidatePrice(
            final TaskExecutor taskExecutor,
            final StepBuilderFactory stepBuilderFactory,
            final ConditionalJobItemReader conditionalJobItemReader,
            final ConditionalJobItemProcessor conditionalJobItemProcessor,
            final ConditionalJobItemWriter conditionalJobItemWriter,
            final PlatformTransactionManager transactionManager,
            final ConditionalStepListener conditionalStepListener
    ) {
        return stepBuilderFactory.get(CONDITIONAL_JOB_NAME + "_invalidatePrice_step")
                .allowStartIfComplete(true)
                .transactionManager(transactionManager)
                .<Price, Price>chunk(CHUNK_SIZE)
                .reader(conditionalJobItemReader)
                .processor(conditionalJobItemProcessor)
                .writer(conditionalJobItemWriter)
                .taskExecutor(taskExecutor)
                .listener(conditionalStepListener)
                .build();
    }

    @Bean
    public Step rollBackInvalidatePrice(
            final TaskExecutor taskExecutor,
            final StepBuilderFactory stepBuilderFactory,
            final ConditionalJobItemReader conditionalJobItemReader,
            final RollBackJobItemProcessor rollBackJobItemProcessor,
            final ConditionalJobItemWriter conditionalJobItemWriter
    ) {
        return stepBuilderFactory.get(CONDITIONAL_JOB_NAME + "_rollBackInvalidatePrice_step")
                .allowStartIfComplete(true)
                .<Price, Price>chunk(CHUNK_SIZE)
                .reader(conditionalJobItemReader)
                .processor(rollBackJobItemProcessor)
                .writer(conditionalJobItemWriter)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public Step applyMonthlyFees(
            final TaskExecutor taskExecutor,
            final StepBuilderFactory stepBuilderFactory,
            final SimpleJobItemReader simpleJobItemReader,
            final MonthlyFeesJobItemProcessor monthlyFeesJobItemProcessor,
            final SimpleJobItemWriter simpleJobItemWriter
    ) {
        return stepBuilderFactory.get(CONDITIONAL_JOB_NAME + "_applyMonthlyFees_step")
                .<Price, Price>chunk(CHUNK_SIZE)
                .reader(simpleJobItemReader)
                .processor(monthlyFeesJobItemProcessor)
                .writer(simpleJobItemWriter)
                .taskExecutor(taskExecutor)
                .throttleLimit(8)
                .build();
    }

    @Bean(name = CONDITIONAL_JOB_NAME)
    public Job conditionalJob(
            final JobBuilderFactory jobBuilderFactory,
            final Step invalidatePrice,
            final Step applyMonthlyFees,
            final Step rollBackInvalidatePrice,
            final ConditionalJobListener conditionalJobListener
    ) {
        return jobBuilderFactory.get(CONDITIONAL_JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .listener(conditionalJobListener)
                .start(invalidatePrice)
                .on("COMPLETED").to(applyMonthlyFees)
                .from(invalidatePrice).on("FAILED").to(rollBackInvalidatePrice)
                .end()
                .build();
    }

    @Bean(name = FAILED_JOB_NAME)
    public Job failedJob(
            final JobBuilderFactory jobBuilderFactory,
            final Step invalidatePrice
    ) {
        return jobBuilderFactory.get(FAILED_JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(invalidatePrice)
                .build();
    }

    @Bean(name = STOP_STEP_JOB_NAME)
    public Job stopAtStepJob(
            final JobBuilderFactory jobBuilderFactory,
            final Flow decideRollBack,
            final Step applyMonthlyFees
    ) {
        return jobBuilderFactory.get(STOP_STEP_JOB_NAME + "_step")
                .start(decideRollBack)
                .on("COMPLETED")
                .stopAndRestart(applyMonthlyFees)
                .end()
                .build();
    }

    @Bean
    public Flow decideRollBack(
            final JobExecutionDecider jobExecutionDecider,
            final Step invalidatePrice,
            final Step rollBackInvalidatePrice
    ) {
        return new FlowBuilder<SimpleFlow>("toRollBack")
                .start(jobExecutionDecider)
                .on("true").to(rollBackInvalidatePrice)
                .from(jobExecutionDecider)
                .on("false").to(invalidatePrice)
                .end();
    }

    @Bean
    public JobExecutionDecider jobExecutionDecider() {
        return ((jobExecution, stepExecution) -> new FlowExecutionStatus(
            jobExecution.getJobParameters().getString("rollBack")
        ));
    }
}
