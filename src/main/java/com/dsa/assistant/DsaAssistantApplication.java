package com.dsa.assistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class DsaAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(DsaAssistantApplication.class, args);
    }
}
