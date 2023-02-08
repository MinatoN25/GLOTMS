package com.glotms.userservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {


    @Bean
    public NewTopic registerTopic(){
    	
        return TopicBuilder.name("registerTopic")
                .partitions(3)
                .replicas(1)
                .build();
    }
    
    @Bean
    public NewTopic OTPMailTopic(){

        return TopicBuilder.name("OTPMailTopic")
                .partitions(3)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic deleteMailTopic(){

        return TopicBuilder.name("delete_user_mail")
                .partitions(3)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic deleteUserTopic(){

        return TopicBuilder.name("delete_user")
                .partitions(3)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic ReactivateMailTopic(){

        return TopicBuilder.name("reactivateMailTopic")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
