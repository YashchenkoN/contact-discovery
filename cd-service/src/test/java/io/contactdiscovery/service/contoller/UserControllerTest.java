package io.contactdiscovery.service.contoller;

import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;

import org.apache.commons.lang3.RandomStringUtils;
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
import io.contactdiscovery.service.api.ActivateDeviceRequest;
import io.contactdiscovery.service.api.IdRef;
import io.contactdiscovery.service.api.RegisterUserRequest;
import io.contactdiscovery.service.entity.User;
import io.contactdiscovery.service.entity.UserStatus;
import io.contactdiscovery.service.repository.UserRepository;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mykola Yashchenko
 */
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ContactDiscoveryServiceApplication.class)
public class UserControllerTest {

    private static WireMockServer wireMockServer = new WireMockServer(8085);

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public static void prepare() {
        wireMockServer.start();

        wireMockServer.stubFor(post(urlEqualTo("/otps"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                )
        );
    }

    private static User user(final String phoneNumber) {
        final User user = new User();
        user.setPhoneNumber(phoneNumber);
        user.setStatus(UserStatus.ACTIVATED);
        return user;
    }

    @Test
    public void shouldCreateContact() {
        final String phoneNumber = "+380" + RandomStringUtils.randomNumeric(9);

        final RegisterUserRequest request = new RegisterUserRequest();
        request.setPhoneNumber(phoneNumber);

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
        final String phoneNumber = "+380" + RandomStringUtils.randomNumeric(9);
        userRepository.save(user(phoneNumber)).block();

        final RegisterUserRequest request = new RegisterUserRequest();
        request.setPhoneNumber(phoneNumber);

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

    @Test
    public void shouldActivateDevice() {
        wireMockServer.stubFor(post(urlEqualTo("/otps/verify"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                )
        );

        final String phoneNumber = "+380" + RandomStringUtils.randomNumeric(9);
        final User user = user(phoneNumber);
        user.setStatus(UserStatus.NOT_ACTIVATED);
        userRepository.save(user).block();

        final ActivateDeviceRequest request = new ActivateDeviceRequest();
        request.setOtp(RandomStringUtils.randomNumeric(9));

        final User oldUser = userRepository.findByPhoneNumber(phoneNumber).block();
        assertThat(oldUser).isNotNull();

        webTestClient.put()
                .uri("/users/" + oldUser.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(request))
                .exchange()
                .expectStatus().isOk();

        final User actualUser = userRepository.findByPhoneNumber(oldUser.getPhoneNumber()).block();
        assertThat(actualUser).isNotNull();
        assertThat(actualUser.getStatus()).isEqualTo(UserStatus.ACTIVATED);
    }

    @Test
    public void shouldReturnErrorIfOtpIsIncorrect() {
        wireMockServer.stubFor(post(urlEqualTo("/otps/verify"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(
                        aResponse()
                                .withBody("{}")
                                .withStatus(400)
                                .withHeader("Content-Type", "application/json")
                )
        );

        final String phoneNumber = "+380" + RandomStringUtils.randomNumeric(9);
        final User user = user(phoneNumber);
        user.setStatus(UserStatus.NOT_ACTIVATED);
        userRepository.save(user).block();

        final ActivateDeviceRequest request = new ActivateDeviceRequest();
        request.setOtp(RandomStringUtils.randomNumeric(9));

        final User oldUser = userRepository.findByPhoneNumber(phoneNumber).block();
        assertThat(oldUser).isNotNull();

        webTestClient.put()
                .uri("/users/" + oldUser.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(request))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void shouldReturnErrorIfDeviceNotFound() {
        final ActivateDeviceRequest request = new ActivateDeviceRequest();
        request.setOtp(RandomStringUtils.randomNumeric(9));

        webTestClient.put()
                .uri("/users/" + UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(request))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void shouldReturnErrorIfRequiredFieldIsMissing() {
        final ActivateDeviceRequest request = new ActivateDeviceRequest();
        request.setOtp("");

        webTestClient.put()
                .uri("/users/" + UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(request))
                .exchange()
                .expectStatus().isBadRequest();
    }
}
