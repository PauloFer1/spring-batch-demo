package com.nutmeg.springbatchdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;

@Configuration
@EnableIntegration
public class RemoteConfiguration {

    @Bean
    public MessageChannel requestChannel() {
        return new DirectChannel();
    }

    @Bean
    public PollableChannel replyChannel() {
        return new QueueChannel();
    }

    @Bean
    public MessagingTemplate messagingTemplate(MessageChannel requestChannel) {
        MessagingTemplate messagingTemplate = new MessagingTemplate(requestChannel);
        messagingTemplate.setReceiveTimeout(1000);
        return messagingTemplate;
    }
}
