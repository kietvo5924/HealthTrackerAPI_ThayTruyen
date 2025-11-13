package com.yourcompany.healthtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HealthTrackerApiApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(HealthTrackerApiApplication.class)
                .properties("spring.config.additional-location=optional:classpath:application-secrets.properties")
                .run(args);
    }

}
