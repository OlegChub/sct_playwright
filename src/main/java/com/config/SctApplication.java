package com.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = {"web", "web.pages"})
@PropertySource("classpath:application.properties")
public class SctApplication {
    public static void main(String[] args) {
        SpringApplication.run(SctApplication.class, args);
    }
}
