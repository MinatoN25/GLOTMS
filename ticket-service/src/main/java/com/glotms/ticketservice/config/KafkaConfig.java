package com.glotms.ticketservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {


    @Bean
    public NewTopic registerTopic(){

        return TopicBuilder.name("addTicketTopic")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
