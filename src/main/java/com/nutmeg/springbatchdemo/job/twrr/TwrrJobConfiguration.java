package com.nutmeg.springbatchdemo.job.twrr;

import com.nutmeg.springbatchdemo.job.twrr.processor.TwrrItemProcessor;
import com.nutmeg.springbatchdemo.job.twrr.reader.TwrrFileItemReader;
import com.nutmeg.springbatchdemo.job.twrr.writer.TwrrItemWriter;
import com.nutmeg.springbatchdemo.model.AccountPostingCsv;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class TwrrJobConfiguration {

    private static final String JOB_NAME = "twrr_data";
    private static final int CHUNK_SIZE = 10;

    @Bean
    public Step twrrFilterData(
            TaskExecutor taskExecutor,
            StepBuilderFactory stepBuilderFactory,
            TwrrFileItemReader twrrFileItemReader,
            TwrrItemProcessor twrrItemProcessor,
            TwrrItemWriter twrrItemWriter
    ) {
        return stepBuilderFactory.get(JOB_NAME + "_step")
                .<AccountPostingCsv, AccountPostingCsv>chunk(CHUNK_SIZE)
                .reader(twrrFileItemReader)
                .processor(twrrItemProcessor)
                .writer(twrrItemWriter)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean(name = JOB_NAME)
    public Job twrrFindBadData(
            JobBuilderFactory jobBuilderFactory,
            Step twrrFilterData
    ) {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .flow(twrrFilterData)
                .end()
                .build();
    }
}