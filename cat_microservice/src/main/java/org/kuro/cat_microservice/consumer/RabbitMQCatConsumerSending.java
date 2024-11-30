package org.kuro.cat_microservice.consumer;

import org.kuro.cat_microservice.repository.CatRepository;
import org.kuro.cat_microservice.repository.OwnerRepository;
import org.kuro.cat_microservice.service.exception.CatNotFoundException;
import org.kuro.cat_microservice.service.mapper.CatMapper;
import org.kuro.entity.Cat;
import org.kuro.entity.dto.CatDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RabbitMQCatConsumerSending {
    private final CatRepository catRepository;
    private final OwnerRepository ownerRepository;
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQCatConsumerSending(CatRepository catRepository, OwnerRepository ownerRepository, RabbitTemplate rabbitTemplate) {
        this.catRepository = catRepository;
        this.ownerRepository = ownerRepository;
        this.rabbitTemplate = rabbitTemplate;
    }


    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQCatConsumerSending.class);

    @RabbitListener(queues = "${rabbitmq.queue.json.get.name}")
    public CatDto getByCatId(UUID catId) {
        LOGGER.info(String.format("Consumer get Message to id: %s", catId.toString()));

        Cat cat = catRepository.findById(catId)
                .orElseThrow(CatNotFoundException::new);

        return CatMapper.convertCatToCatDto(cat);
    }

    @RabbitListener(queues = "${rabbitmq.queue.json.get.by.name.name}")
    public CatDto getCatByName(String name) {
        LOGGER.info(String.format("Consumer get Message to name: %s", name));

        List<Cat> cats = catRepository.findByName(name);
        if (cats.isEmpty()) {
            throw new CatNotFoundException(name);
        } else {
            return CatMapper.convertCatToCatDto(cats.getFirst());
        }
    }

    @RabbitListener(queues = {"${rabbitmq.queue.json.create.name}"})
    public void createCat(CatDto catDto){
        LOGGER.info(String.format("Consumer Json Message to create: %s", catDto.toString()));

        catRepository.save(CatMapper.convertCatDtoToCat(catDto, ownerRepository, catRepository));
    }

    @RabbitListener(queues = {"${rabbitmq.queue.json.update.name}"})
    public void updateCat(CatDto catDto){
        LOGGER.info(String.format("Consumer Json Message to update: %s", catDto.toString()));

        catRepository.findById(catDto.getId()).ifPresent(c -> {
            catRepository.delete(c);
            catRepository.save(CatMapper.convertCatDtoToCat(catDto, ownerRepository, catRepository));
        });
    }

    @RabbitListener(queues = {"${rabbitmq.queue.json.delete.name}"})
    public void deleteCat(CatDto catDto){
        LOGGER.info(String.format("Consumer Json Message to delete: %s", catDto.toString()));

        catRepository
                .findById(catDto.getId())
                .ifPresent(catRepository::delete);
    }
}