package org.kuro.getaway_microservice.publisher;

import org.kuro.entity.dto.OwnerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class RabbitMQOwnerProducerSending {

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

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQOwnerProducerSending.class);

    private RabbitTemplate rabbitTemplate;

    public RabbitMQOwnerProducerSending(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public OwnerDto getByOwnerId(UUID ownerId) {
        LOGGER.info("Get owner by id: {}", ownerId);

        return (OwnerDto) Objects.requireNonNull(rabbitTemplate.convertSendAndReceive(exchange, routingJsonGetKey, ownerId));
    }

    public OwnerDto getOwnerByName(String ownerName) {
        LOGGER.info("Get owner by name: {}", ownerName);

        return (OwnerDto) Objects.requireNonNull(rabbitTemplate.convertSendAndReceive(exchange, routingJsonGetByNameKey, ownerName));
    }

    public void createOwner(OwnerDto ownerDto){
        LOGGER.info(String.format("Sending json message to create -> %s", ownerDto.toString()));

        rabbitTemplate.convertAndSend(exchange, routingJsonCreateKey, ownerDto);
    }

    public void updateOwner(OwnerDto ownerDto){
        LOGGER.info(String.format("Sending json message to update -> %s", ownerDto.toString()));

        rabbitTemplate.convertAndSend(exchange, routingJsonUpdateKey, ownerDto);
    }

    public void deleteOwner(OwnerDto ownerDto){
        LOGGER.info(String.format("Sending json message to delete -> %s", ownerDto.toString()));

        rabbitTemplate.convertAndSend(exchange, routingJsonDeleteKey, ownerDto);
    }
}
