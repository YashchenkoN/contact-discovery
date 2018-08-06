package io.contactdiscovery.service.contoller;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeAll;
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
import io.contactdiscovery.service.api.IdRef;
import io.contactdiscovery.service.api.RegisterUserRequest;
import io.contactdiscovery.service.entity.User;
import io.contactdiscovery.service.entity.UserStatus;
import io.contactdiscovery.service.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mykola Yashchenko
 */
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ContactDiscoveryServiceApplication.class)
public class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public static void prepareMongo(@Autowired final UserRepository userRepository) {
        userRepository.save(user("+380955151515")).block();
    }

    private static User user(final String phoneNumber) {
        final User user = new User();
        user.setPhoneNumber(phoneNumber);
        user.setStatus(UserStatus.ACTIVATED);
        return user;
    }

    @Test
    public void shouldCreateContact() {
        final RegisterUserRequest request = new RegisterUserRequest();
        request.setPhoneNumber("+380955511111");

        webTestClient.post()
                .uri("/users")
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

                        final User user = userRepository.findById(idRef.getId()).block();
                        assertThat(user).isNotNull();
                        assertThat(user.getPhoneNumber()).isEqualTo(request.getPhoneNumber());
                        assertThat(user.getStatus()).isEqualTo(UserStatus.NOT_ACTIVATED);
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                });

    }

    @Test
    public void shouldReturnErrorIfFieldIsEmpty() {
        final RegisterUserRequest request = new RegisterUserRequest();
        request.setPhoneNumber("");

        webTestClient.post()
                .uri("/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(request))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.error").isEqualTo("Bad Request");
    }

    @Test
    public void shouldReplaceExistingNumber() {
        final RegisterUserRequest request = new RegisterUserRequest();
        request.setPhoneNumber("+380955151515");

        final User oldUser = userRepository.findByPhoneNumber(request.getPhoneNumber()).block();
        assertThat(oldUser).isNotNull();

        webTestClient.post()
                .uri("/users")
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

                        final User user = userRepository.findById(idRef.getId()).block();
                        assertThat(user).isNotNull();
                        assertThat(user.getId()).isNotEqualTo(oldUser.getId());
                        assertThat(user.getPhoneNumber()).isEqualTo(request.getPhoneNumber());
                        assertThat(user.getStatus()).isEqualTo(UserStatus.NOT_ACTIVATED);

                        assertThat(userRepository.findById(oldUser.getId()).block()).isNull();
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
