package io.contactdiscovery.service.config;

import java.util.UUID;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import io.contactdiscovery.service.entity.Contact;
import io.contactdiscovery.service.repository.ContactRepository;

/**
 * @author Mykola Yashchenko
 */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@EnableReactiveMongoRepositories
@AutoConfigureAfter(EmbeddedMongoAutoConfiguration.class)
public class EmbeddedMongo extends AbstractReactiveMongoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddedMongo.class);

    @Autowired
    private ContactRepository contactRepository;

    @Override
    protected String getDatabaseName() {
        return "contacts_test";
    }

    @Bean
    @Override
    public MongoClient reactiveMongoClient() {
        return MongoClients.create(String.format("mongodb://localhost:%d", 27015));
    }

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