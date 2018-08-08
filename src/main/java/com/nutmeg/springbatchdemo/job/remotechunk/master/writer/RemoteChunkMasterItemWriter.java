package com.nutmeg.springbatchdemo.job.remotechunk.master.writer;

import com.nutmeg.springbatchdemo.model.Price;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.integration.chunk.ChunkMessageChannelItemWriter;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.PollableChannel;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Slf4j
@Named
public class RemoteChunkMasterItemWriter extends ChunkMessageChannelItemWriter<Price> {

    @Inject
    public RemoteChunkMasterItemWriter(final MessagingTemplate messagingTemplate, PollableChannel replyChannel) {
        super.setMessagingOperations(messagingTemplate);
        super.setReplyChannel(replyChannel);
    }


    @Override
    public void write(List<? extends Price> items) throws Exception {
        log.info("writing {} items to channel.", items.size());
        super.write(items);
    }
}
