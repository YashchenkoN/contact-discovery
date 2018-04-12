package io.contactdiscovery.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @author Mykola Yashchenko
 */
@EnableConfigServer
@SpringBootApplication
public class ContactDiscoveryConfig {

    public static void main(String[] args) {
        SpringApplication.run(ContactDiscoveryConfig.class, args);
    }
}
