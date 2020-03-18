package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
public class DatabaseSetUp {

    @Bean
    CommandLineRunner initDatabase(GameRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                //New empty database, add some persons
                log.info("Added to database " + repository.save(new Game(0L, "Dragon Age", "Bioware")));
                log.info("Added to database " + repository.save(new Game(0L, "Animal Crossing", "Nintendo")));
            }
        };
    }

    @Bean
    @LoadBalanced
    RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
