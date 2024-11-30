package org.kuro.getaway_microservice.controller;

import org.kuro.entity.dto.CatDto;
import org.kuro.getaway_microservice.publisher.RabbitMQCatProducerSending;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cats")
public class CatController {

    private RabbitMQCatProducerSending jsonProducer;

    public CatController(RabbitMQCatProducerSending jsonProducer) {
        this.jsonProducer = jsonProducer;
    }

    @GetMapping("/{catId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CatDto getByCatId(@PathVariable UUID catId) {
        return jsonProducer.getByCatId(catId);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public CatDto getCatByName(@PathVariable String name) {
        return jsonProducer.getCatByName(name);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createCat(@RequestBody CatDto catDto) {
        jsonProducer.createCat(catDto);
        return ResponseEntity.ok("Json message sent to RabbitMQ ...");
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateCat(@RequestBody CatDto catDto) {
        jsonProducer.updateCat(catDto);
        return ResponseEntity.ok("Json message sent to RabbitMQ ...");
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteCat(@RequestBody CatDto catDto) {
        jsonProducer.deleteCat(catDto);
        return ResponseEntity.ok("Json message sent to RabbitMQ ...");
    }
}
