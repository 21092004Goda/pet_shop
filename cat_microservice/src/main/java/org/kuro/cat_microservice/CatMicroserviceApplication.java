package org.kuro.cat_microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "org.kuro.cat_microservice.repository")
@EntityScan(basePackages = "org.kuro.entity")
@SpringBootApplication(scanBasePackages = {"org.kuro.cat_microservice.*"})
public class CatMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatMicroserviceApplication.class, args);
    }

}
