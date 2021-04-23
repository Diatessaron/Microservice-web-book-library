package ru.otus.webbooklibrary;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringBootApplication
@EnableMongock
@EnableCircuitBreaker
public class WebBookLibraryService {
    public static void main(String[] args) {
        SpringApplication.run(WebBookLibraryService.class, args);
    }
}
