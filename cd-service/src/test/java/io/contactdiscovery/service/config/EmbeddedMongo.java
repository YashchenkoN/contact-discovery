package io.contactdiscovery.service.config;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.contactdiscovery.service.entity.Contact;
import io.contactdiscovery.service.repository.ContactRepository;

/**
 * @author Mykola Yashchenko
 */
@Configuration
public class EmbeddedMongo {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddedMongo.class);

    @Autowired
    private ContactRepository contactRepository;

    @Bean
    public CommandLineRunner createDb() {
        return args -> {
            LOGGER.info("INSERTING CONTACTS...");
            contactRepository.save(contact("1")).block();
        };
    }

    private Contact contact(final String id) {
        final Contact contact = new Contact();
        contact.setId(id);
        contact.setFistName(UUID.randomUUID().toString());
        contact.setLastName(UUID.randomUUID().toString());
        contact.setNumber(UUID.randomUUID().toString());
        return contact;
    }
}