package io.contactdiscovery.query.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.contactdiscovery.query.entity.Contact;
import io.contactdiscovery.query.service.ContactService;
import reactor.core.publisher.Flux;

/**
 * @author Mykola Yashchenko
 */
@RestController
@RequestMapping(value = "/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(final ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public Flux<Contact> findContacts(@RequestParam("fullName") final String fullName,
                                      @RequestParam(value = "page", defaultValue = "1") final Integer page,
                                      @RequestParam(value = "size", defaultValue = "30") final Integer size) {
        return contactService.findContacts(fullName, PageRequest.of(page, size));
    }
}
