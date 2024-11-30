package org.kuro.owner_microservice.consumer;

import org.kuro.entity.Owner;
import org.kuro.entity.dto.OwnerDto;
import org.kuro.owner_microservice.repository.CatRepository;
import org.kuro.owner_microservice.repository.OwnerRepository;
import org.kuro.owner_microservice.service.exception.OwnerNotFoundException;
import org.kuro.owner_microservice.service.mapper.OwnerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RabbitMQOwnerConsumerSending {
    private final OwnerRepository ownerRepository;
    private final CatRepository catRepository;
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQOwnerConsumerSending(OwnerRepository ownerRepository, CatRepository catRepository, RabbitTemplate rabbitTemplate) {
        this.ownerRepository = ownerRepository;
        this.catRepository = catRepository;
        this.rabbitTemplate = rabbitTemplate;
    }


    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQOwnerConsumerSending.class);

    @RabbitListener(queues = "${rabbitmq.queue.password.name}")
    public OwnerDto passwordOwner(String username) {
        LOGGER.info(String.format("Consumer get User to name: %s", username));

        List<Owner> owners = ownerRepository.findByName(username);
        if (owners.isEmpty()) {
            throw new OwnerNotFoundException(username);
        } else {
            return OwnerMapper.convertToDTO(owners.getFirst());
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.json.get.name}")
    public OwnerDto getByOwnerId(UUID ownerId) {
        LOGGER.info(String.format("Consumer get Message to id: %s", ownerId.toString()));

        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(OwnerNotFoundException::new);

        return OwnerMapper.convertToDTO(owner);
    }

    @RabbitListener(queues = "${rabbitmq.queue.json.get.by.name.name}")
    public OwnerDto getOwnerByName(String name) {
        LOGGER.info(String.format("Consumer get Message to name: %s", name));

        List<Owner> owners = ownerRepository.findByName(name);
        if (owners.isEmpty()) {
            throw new OwnerNotFoundException(name);
        } else {
            return OwnerMapper.convertToDTO(owners.getFirst());
        }
    }

    @RabbitListener(queues = {"${rabbitmq.queue.json.create.name}"})
    public void createOwner(OwnerDto ownerDto){
        LOGGER.info(String.format("Consumer Json Message to create: %s", ownerDto.toString()));

        ownerRepository.save(OwnerMapper.convertToEntity(ownerDto, catRepository));
    }

    @RabbitListener(queues = {"${rabbitmq.queue.json.update.name}"})
    public void updateOwner(OwnerDto ownerDto){
        LOGGER.info(String.format("Consumer Json Message to update: %s", ownerDto.toString()));

        ownerRepository.findById(ownerDto.getId()).ifPresent(owner -> {
            ownerRepository.delete(owner);
            ownerRepository.save(OwnerMapper.convertToEntity(ownerDto, catRepository));
        });
    }

    @RabbitListener(queues = {"${rabbitmq.queue.json.delete.name}"})
    public void deleteOwner(OwnerDto ownerDto){
        LOGGER.info(String.format("Consumer Json Message to delete: %s", ownerDto.toString()));

        ownerRepository
                .findById(ownerDto.getId())
                .ifPresent(ownerRepository::delete);
    }
}
