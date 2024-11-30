package org.kuro.owner_microservice.service.exception;

public class EntityNotFoundException extends IllegalArgumentException {
    public EntityNotFoundException(String entity, String additionalInfo) {
        super(String.format("Entity %s was not found: %s", entity, additionalInfo));
    }
}