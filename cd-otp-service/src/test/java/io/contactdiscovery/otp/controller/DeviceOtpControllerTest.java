package io.contactdiscovery.otp.controller;

import java.io.IOException;
import java.util.UUID;

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

import io.contactdiscovery.otp.ContactDiscoveryOtpServiceApplication;
import io.contactdiscovery.otp.api.IdRef;
import io.contactdiscovery.otp.api.RegisterDeviceOtp;
import io.contactdiscovery.otp.entity.DeviceOtp;
import io.contactdiscovery.otp.repository.DeviceOtpRepository;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Mykola Yashchenko
 */
@AutoConfigureWebTestClient
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ContactDiscoveryOtpServiceApplication.class)
public class DeviceOtpControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DeviceOtpRepository deviceOtpRepository;

    @BeforeAll
    public static void prepareMongo(@Autowired final DeviceOtpRepository deviceOtpRepository) {
        deviceOtpRepository.save(deviceOtp("1")).block();
    }

    private static DeviceOtp deviceOtp(final String deviceId) {
        final DeviceOtp deviceOtp = new DeviceOtp();
        deviceOtp.setDeviceId(deviceId);
        deviceOtp.setSeed(UUID.randomUUID().toString());
        return deviceOtp;
    }

    @Test
    public void shouldGenerateOtp() {
        final RegisterDeviceOtp request = new RegisterDeviceOtp();
        request.setDeviceId(UUID.randomUUID().toString());

        webTestClient.post()
                .uri("/otps")
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

                        final DeviceOtp deviceOtp = deviceOtpRepository.findById(idRef.getId()).block();
                        assertThat(deviceOtp).isNotNull();
                        assertThat(deviceOtp.getDeviceId()).isEqualTo(request.getDeviceId());
                        assertThat(deviceOtp.getSeed()).isNotBlank();
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Test
    public void shouldRegenerateOtp() {
        final RegisterDeviceOtp request = new RegisterDeviceOtp();
        request.setDeviceId("1");

        final DeviceOtp oldDeviceOtp = deviceOtpRepository.findByDeviceId("1").block();
        assertThat(oldDeviceOtp).isNotNull();
        assertThat(oldDeviceOtp.getDeviceId()).isEqualTo(request.getDeviceId());
        assertThat(oldDeviceOtp.getSeed()).isNotBlank();

        webTestClient.post()
                .uri("/otps")
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

                        final DeviceOtp deviceOtp = deviceOtpRepository.findById(idRef.getId()).block();
                        assertThat(deviceOtp).isNotNull();
                        assertThat(deviceOtp.getId()).isEqualTo(oldDeviceOtp.getId());
                        assertThat(deviceOtp.getDeviceId()).isEqualTo(request.getDeviceId());
                        assertThat(deviceOtp.getSeed()).isNotEqualTo(oldDeviceOtp.getSeed());
                    } catch (final IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
