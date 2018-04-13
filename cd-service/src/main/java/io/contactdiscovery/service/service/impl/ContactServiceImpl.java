package io.contactdiscovery.service.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import io.contactdiscovery.service.api.CreateContactRequest;
import io.contactdiscovery.service.entity.Contact;
import io.contactdiscovery.service.repository.ContactRepository;
import io.contactdiscovery.service.service.ContactService;
import reactor.core.publisher.Mono;

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
    public Mono<String> create(final CreateContactRequest request) {
        final Contact contact = request.toContact();
        contact.setId(UUID.randomUUID().toString());

        return contactRepository.insert(contact)
                .map(Contact::getId);
    }
}
