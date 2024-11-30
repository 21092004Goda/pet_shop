package org.kuro.getaway_microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class GetawayMicroserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(GetawayMicroserviceApplication.class, args);
    }
}
