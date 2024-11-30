package org.kuro.getaway_microservice.publisher;

import org.kuro.entity.dto.CatDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class RabbitMQCatProducerSending {

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

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQCatProducerSending.class);

    private RabbitTemplate rabbitTemplate;

    public RabbitMQCatProducerSending(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public CatDto getByCatId(UUID catId) {
        LOGGER.info("Get cat by id: {}", catId);

        return (CatDto) Objects.requireNonNull(rabbitTemplate.convertSendAndReceive(exchange, routingJsonGetKey, catId));
    }

    public CatDto getCatByName(String catName) {
        LOGGER.info("Get cat by name: {}", catName);

        return (CatDto) Objects.requireNonNull(rabbitTemplate.convertSendAndReceive(exchange, routingJsonGetByNameKey, catName));
    }

    public void createCat(CatDto catDto){
        LOGGER.info(String.format("Sending json message to create -> %s", catDto.toString()));

        rabbitTemplate.convertAndSend(exchange, routingJsonCreateKey, catDto);
    }

    public void updateCat(CatDto catDto){
        LOGGER.info(String.format("Sending json message to update -> %s", catDto.toString()));

        rabbitTemplate.convertAndSend(exchange, routingJsonUpdateKey, catDto);
    }

    public void deleteCat(CatDto catDto){
        LOGGER.info(String.format("Sending json message to delete -> %s", catDto.toString()));

        rabbitTemplate.convertAndSend(exchange, routingJsonDeleteKey, catDto);
    }
}
