package com.numble.numdeal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class NumdealApplication {

    public static void main(String[] args) {
        SpringApplication.run(NumdealApplication.class, args);
    }

}
