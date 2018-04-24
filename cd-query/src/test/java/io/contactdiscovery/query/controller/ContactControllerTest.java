package io.contactdiscovery.query.controller;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import io.contactdiscovery.query.ContactDiscoveryQueryApplication;
import io.contactdiscovery.query.entity.Contact;
import io.contactdiscovery.query.repository.ContactRepository;

/**
 * @author Mykola Yashchenko
 */
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ContactDiscoveryQueryApplication.class)
public class ContactControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @BeforeAll
    public static void insertData(@Autowired final ContactRepository contactRepository) {
        contactRepository.save(contact("123"));

        for (int i = 0; i < 50; i++) {
            contactRepository.save(contact("123" + UUID.randomUUID().toString()));
        }
    }

    private static Contact contact(final String fullName) {
        var contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        contact.setNumber(UUID.randomUUID().toString());
        contact.setFullName(fullName);
        return contact;
    }

    @Test
    public void shouldFindByFullName() {
        webTestClient.get()
                .uri("/contacts?fullName=123")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isNotEmpty()
                .jsonPath("$[0].number").isNotEmpty()
                .jsonPath("$[0].fullName").isEqualTo("123");
    }
}
