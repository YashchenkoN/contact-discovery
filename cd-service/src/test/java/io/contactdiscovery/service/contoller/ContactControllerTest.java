package io.contactdiscovery.service.contoller;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import io.contactdiscovery.service.ContactDiscoveryServiceApplication;
import io.contactdiscovery.service.api.CreateContactRequest;
import io.contactdiscovery.service.api.IdRef;
import io.contactdiscovery.service.entity.Contact;
import io.contactdiscovery.service.repository.ContactRepository;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mykola Yashchenko
 */
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ContactDiscoveryServiceApplication.class)
public class ContactControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
                .expectBody().jsonPath("$.id").isNotEmpty()
                .consumeWith(result -> {
                    final var responseBody = result.getResponseBody();
                    try {
                        final IdRef idRef = objectMapper.readValue(new String(responseBody), IdRef.class);
                        assertThat(idRef).isNotNull();

                        final Contact contact = contactRepository.findById(idRef.getId()).block();
                        assertThat(contact).isNotNull();
                        assertThat(contact.getNumber()).isEqualTo(request.getNumber());
                        assertThat(contact.getFistName()).isEqualTo(request.getFistName());
                        assertThat(contact.getLastName()).isEqualTo(request.getLastName());
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                });

    }

    @ParameterizedTest
    @MethodSource("emptyFieldsProvider")
    public void shouldReturnErrorIfFieldIsEmpty(final String number, final String firstName, final String lastName) {
        final CreateContactRequest request = new CreateContactRequest();
        request.setFistName(firstName);
        request.setLastName(lastName);
        request.setNumber(number);

        webTestClient.post()
                .uri("/contacts")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(request))
                .exchange()
                .expectStatus().isBadRequest();
    }

    private static Stream<Arguments> emptyFieldsProvider() {
        return Stream.of(
                Arguments.of("", UUID.randomUUID().toString(), UUID.randomUUID().toString()),
                Arguments.of(UUID.randomUUID().toString(), "", UUID.randomUUID().toString()),
                Arguments.of(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "")
        );
    }
}
