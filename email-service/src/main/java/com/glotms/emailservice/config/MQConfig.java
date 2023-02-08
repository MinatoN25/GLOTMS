package com.glotms.emailservice.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
	
	public static final String MAIL_EXCHANGE = "mail_exchange";

    public static final String REGISTER_MAIL_QUEUE = "register_mail_queue";  
    public static final String REGISTER_MAIL_ROUTING_KEY = "register_mail_routingKey";
    
    public static final String DELETE_ACCOUNT_MAIL_QUEUE = "delete_account_mail_queue";  
    public static final String DELETE_ACCOUNT_MAIL_ROUTING_KEY = "delete_account_routingKey";
    
    public static final String REACTIVATE_ACCOUNT_MAIL_QUEUE = "reactivate_account_mail_queue";  
    public static final String REACTIVATE_ACCOUNT_MAIL_ROUTING_KEY = "reactivate_account_mail_routingKey";
    

    @Bean
    public Queue registerMailQueue() {
        return  new Queue(REGISTER_MAIL_QUEUE);
    }
    
    @Bean
    public Queue deleteAccountMailQueue() {
        return  new Queue(DELETE_ACCOUNT_MAIL_QUEUE);
    }
    

    @Bean
    public Queue reactivateAccountMailQueue() {
        return  new Queue(REACTIVATE_ACCOUNT_MAIL_QUEUE);
    }

    @Bean
    public TopicExchange mailExchange() {
        return new TopicExchange(MAIL_EXCHANGE);
    }

    @Bean
    public Binding registerMailBinding() {
        return BindingBuilder
                .bind(registerMailQueue())
                .to(mailExchange())
                .with(REGISTER_MAIL_ROUTING_KEY);
    }
    
    @Bean
    public Binding deleteAccountMailBinding() {
        return BindingBuilder
                .bind(deleteAccountMailQueue())
                .to(mailExchange())
                .with(DELETE_ACCOUNT_MAIL_ROUTING_KEY);
    }
    
    @Bean
    public Binding reactivateAccountMailBinding() {
        return BindingBuilder
                .bind(reactivateAccountMailQueue())
                .to(mailExchange())
                .with(REACTIVATE_ACCOUNT_MAIL_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return  new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return  template;
    }

}