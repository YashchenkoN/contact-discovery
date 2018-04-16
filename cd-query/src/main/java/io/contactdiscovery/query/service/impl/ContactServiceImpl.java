package io.contactdiscovery.query.service.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.contactdiscovery.query.entity.Contact;
import io.contactdiscovery.query.repository.ContactRepository;
import io.contactdiscovery.query.service.ContactService;
import reactor.core.publisher.Flux;

/**
 * @author Mykola Yashchenko
 */
@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    public ContactServiceImpl(final ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public Flux<Contact> findContacts(final String fullName, final Pageable pageable) {
        return Flux.fromStream(
                contactRepository.findByFullName(fullName, pageable)
        );
    }
}
