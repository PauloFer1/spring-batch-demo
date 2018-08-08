package com.nutmeg.springbatchdemo.config;

import com.nutmeg.springbatchdemo.job.simple.reader.SimpleJobItemReader;
import com.nutmeg.springbatchdemo.model.Price;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.integration.chunk.ChunkMessageChannelItemWriter;
import org.springframework.batch.integration.chunk.RemoteChunkHandlerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class RemoteChunkMasterConfiguration {

    private static final String REMOTE_CHUNK_JOB = "remote_chunk_job";
    private static final String STEP_NAME = "_step";

    @Bean
    public TaskletStep remoteChunkStep(
            final StepBuilderFactory steps,
            final SimpleJobItemReader simpleJobItemReader,
            final ChunkMessageChannelItemWriter<Price> remoteChunkMasterItemWriter,
            final TaskExecutor taskExecutor) {
        return steps.get(REMOTE_CHUNK_JOB + STEP_NAME)
                .<Price, Price>chunk(100)
                .reader(simpleJobItemReader)
                .writer(remoteChunkMasterItemWriter)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public RemoteChunkHandlerFactoryBean<Price> remoteChunkHandlerFactory(
            final ChunkMessageChannelItemWriter<Price> commandChunkMessageItemWriter,
            final TaskletStep commandStep) {
        RemoteChunkHandlerFactoryBean<Price> factory = new RemoteChunkHandlerFactoryBean<>();
        factory.setChunkWriter(commandChunkMessageItemWriter);
        factory.setStep(commandStep);
        return factory;
    }

    @Bean(name = REMOTE_CHUNK_JOB)
    public Job remoteChunkJob(final JobBuilderFactory jobs,
                          final TaskletStep remoteChunkStep) {
        return jobs.get(REMOTE_CHUNK_JOB)
                .start(remoteChunkStep)
                .build();
    }

}
