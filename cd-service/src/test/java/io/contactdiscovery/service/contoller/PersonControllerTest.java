package io.contactdiscovery.service.contoller;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import io.contactdiscovery.service.ContactDiscoveryServiceApplication;
import io.contactdiscovery.service.api.CreateContactRequest;

/**
 * @author Mykola Yashchenko
 */
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ContactDiscoveryServiceApplication.class)
public class PersonControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void shouldCreateContact() {
        final CreateContactRequest request = new CreateContactRequest();
        request.setFistName(UUID.randomUUID().toString());
        request.setLastName(UUID.randomUUID().toString());
        request.setNumber(UUID.randomUUID().toString());

        webTestClient.post()
                .uri("/contacts")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(request))
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.id").isNotEmpty();

    }
}
