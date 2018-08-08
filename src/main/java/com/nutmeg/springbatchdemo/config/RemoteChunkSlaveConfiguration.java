package com.nutmeg.springbatchdemo.config;

import com.nutmeg.springbatchdemo.job.remotechunk.slave.processor.RemoteChunkSlaveItemProcessor;
import com.nutmeg.springbatchdemo.job.remotechunk.slave.writer.RemoteChunkSlaveItemWriter;
import com.nutmeg.springbatchdemo.model.Price;
import org.springframework.batch.core.step.item.SimpleChunkProcessor;
import org.springframework.batch.integration.chunk.ChunkProcessorChunkHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;

@Configuration
public class RemoteChunkSlaveConfiguration {

    @Bean
    @ServiceActivator(inputChannel = "requestChannel", outputChannel = "replyChannel")
    public ChunkProcessorChunkHandler<Price> commandChunkHandler(
            final RemoteChunkSlaveItemProcessor remoteChunkSlaveItemProcessor,
            final RemoteChunkSlaveItemWriter remoteChunkSlaveItemWriter) {
        ChunkProcessorChunkHandler<Price> chunkHandler = new ChunkProcessorChunkHandler<>();
        chunkHandler.setChunkProcessor(new SimpleChunkProcessor<>(remoteChunkSlaveItemProcessor, remoteChunkSlaveItemWriter));
        return chunkHandler;
    }
}
