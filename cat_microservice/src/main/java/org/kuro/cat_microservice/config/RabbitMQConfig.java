package org.kuro.cat_microservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.json.get.name}")
    private String jsonGetQueue;

    @Value("${rabbitmq.queue.json.get.by.name.name}")
    private String jsonGetByNameQueue;

    @Value("${rabbitmq.queue.json.create.name}")
    private String jsonCreateQueue;

    @Value("${rabbitmq.queue.json.update.name}")
    private String jsonUpdateQueue;

    @Value("${rabbitmq.queue.json.delete.name}")
    private String jsonDeleteQueue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.json.get.key}")
    private String routingJsonGetKey;

    @Value("${rabbitmq.routing.json.get.by.name.key}")
    private String routingJsonGetByNameKey;

    @Value("${rabbitmq.routing.json.create.key}")
    private String routingJsonCreateKey;

    @Value("${rabbitmq.routing.json.update.key}")
    private String routingJsonUpdateKey;

    @Value("${rabbitmq.routing.json.delete.key}")
    private String routingJsonDeleteKey;

    @Bean
    public Queue GetQueue() {
        return new Queue(jsonGetQueue);
    }

    @Bean
    public Queue GetByNameQueue() {
        return new Queue(jsonGetByNameQueue);
    }

    @Bean
    public Queue createQueue() {
        return new Queue(jsonCreateQueue);
    }

    @Bean
    public Queue updateQueue() {
        return new Queue(jsonUpdateQueue);
    }

    @Bean
    public Queue daleteQueue() {
        return new Queue(jsonDeleteQueue);
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding jsonGetBinding(){
        return BindingBuilder
                .bind(GetQueue())
                .to(exchange())
                .with(routingJsonGetKey);
    }

    @Bean
    public Binding jsonGetByNameBinding(){
        return BindingBuilder
                .bind(GetByNameQueue())
                .to(exchange())
                .with(routingJsonGetByNameKey);
    }

    @Bean
    public Binding jsonCreateBinding(){
        return BindingBuilder
                .bind(createQueue())
                .to(exchange())
                .with(routingJsonCreateKey);
    }

    @Bean
    public Binding jsonUpdateBinding(){
        return BindingBuilder
                .bind(updateQueue())
                .to(exchange())
                .with(routingJsonUpdateKey);
    }

    @Bean
    public Binding jsonDeleteBinding(){
        return BindingBuilder
                .bind(daleteQueue())
                .to(exchange())
                .with(routingJsonDeleteKey);
    }

    @Bean
    public MessageConverter jsonConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();

        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("org.kuro.entity.dto.CatDto", org.kuro.entity.dto.CatDto.class);

        typeMapper.setIdClassMapping(idClassMapping);
        converter.setJavaTypeMapper(typeMapper);

        return converter;
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonConverter());
        return rabbitTemplate;
    }
}
