package io.contactdiscovery.service.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import io.contactdiscovery.service.entity.Contact;

/**
 * @author Mykola Yashchenko
 */
public interface ContactRepository extends ReactiveMongoRepository<Contact, String> {
}
