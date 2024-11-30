package org.kuro.getaway_microservice.controller;

import org.kuro.entity.dto.OwnerDto;
import org.kuro.getaway_microservice.publisher.RabbitMQOwnerProducerSending;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/owners")
public class OwnerController {

    private RabbitMQOwnerProducerSending jsonProducer;

    public OwnerController(RabbitMQOwnerProducerSending jsonProducer) {
        this.jsonProducer = jsonProducer;
    }

    @GetMapping("/{ownerId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OwnerDto getByOwnerId(@PathVariable UUID ownerId) {
        return jsonProducer.getByOwnerId(ownerId);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public OwnerDto getOwnerByName(@PathVariable String name) {
        return jsonProducer.getOwnerByName(name);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createOwner(@RequestBody OwnerDto ownerDto) {
        jsonProducer.createOwner(ownerDto);
        return ResponseEntity.ok("Json message sent to RabbitMQ ...");
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateOwner(@RequestBody OwnerDto ownerDto) {
        jsonProducer.updateOwner(ownerDto);
        return ResponseEntity.ok("Json message sent to RabbitMQ ...");
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteOwner(@RequestBody OwnerDto ownerDto) {
        jsonProducer.deleteOwner(ownerDto);
        return ResponseEntity.ok("Json message sent to RabbitMQ ...");
    }
}
