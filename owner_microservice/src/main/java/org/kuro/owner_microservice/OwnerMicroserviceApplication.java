package org.kuro.owner_microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "org.kuro.owner_microservice.repository")
@EntityScan(basePackages = "org.kuro.entity")
@SpringBootApplication(scanBasePackages = {"org.kuro.owner_microservice.*"})
public class OwnerMicroserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OwnerMicroserviceApplication.class, args);
    }
}
