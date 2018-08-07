package io.contactdiscovery.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class ContactDiscoveryServiceApplication {

    public static void main(final String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors", "0");
        SpringApplication.run(ContactDiscoveryServiceApplication.class, args);
    }
}
