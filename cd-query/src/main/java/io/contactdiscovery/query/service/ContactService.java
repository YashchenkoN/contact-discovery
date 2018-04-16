package io.contactdiscovery.query.service;

import org.springframework.data.domain.Pageable;

import io.contactdiscovery.query.entity.Contact;
import reactor.core.publisher.Flux;

/**
 * @author Mykola Yashchenko
 */
public interface ContactService {
    Flux<Contact> findContacts(String fullName, Pageable pageable);
}
