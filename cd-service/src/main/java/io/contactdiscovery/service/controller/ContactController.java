package io.contactdiscovery.service.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.contactdiscovery.service.api.CreateContactRequest;
import io.contactdiscovery.service.api.IdRef;
import io.contactdiscovery.service.service.ContactService;
import reactor.core.publisher.Mono;

@RestController
public class ContactController {

    private final ContactService contactService;

    public ContactController(final ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/contacts")
    public Mono<IdRef> create(@RequestBody @Valid final Mono<CreateContactRequest> request) {
        return request
                .flatMap(contactService::create)
                .map(IdRef::new);
    }

    @DeleteMapping("/contacts/{id}")
    public Mono<Void> delete(@PathVariable("id") final String id) {
        return contactService.delete(id);
    }
}
