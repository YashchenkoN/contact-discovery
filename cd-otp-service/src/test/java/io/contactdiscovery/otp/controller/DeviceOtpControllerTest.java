package io.contactdiscovery.otp.controller;

import java.util.Base64;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
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
import io.contactdiscovery.otp.api.RegisterDeviceOtpRequest;
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
    private DeviceOtpRepository deviceOtpRepository;

    private static DeviceOtp deviceOtp(final String deviceId) {
        final DeviceOtp deviceOtp = new DeviceOtp();
        deviceOtp.setDeviceId(deviceId);
        deviceOtp.setSeed(Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()));
        return deviceOtp;
    }

    @Test
    public void shouldGenerateOtp() {
        final RegisterDeviceOtpRequest request = new RegisterDeviceOtpRequest();
        request.setDeviceId(UUID.randomUUID().toString());

        webTestClient.post()
                .uri("/otps")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(request))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(result -> {
                    final DeviceOtp deviceOtp = deviceOtpRepository.findByDeviceId(request.getDeviceId()).block();
                    assertThat(deviceOtp).isNotNull();
                    assertThat(deviceOtp.getDeviceId()).isEqualTo(request.getDeviceId());
                    assertThat(deviceOtp.getSeed()).isNotEmpty();
                });
    }

    @Test
    public void shouldRegenerateOtp() {
        final String deviceId = RandomStringUtils.randomNumeric(10);

        deviceOtpRepository.save(deviceOtp(deviceId)).block();

        final RegisterDeviceOtpRequest request = new RegisterDeviceOtpRequest();
        request.setDeviceId(deviceId);

        final DeviceOtp oldDeviceOtp = deviceOtpRepository.findByDeviceId(deviceId).block();
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
                .expectBody()
                .consumeWith(result -> {
                    final DeviceOtp deviceOtp = deviceOtpRepository.findByDeviceId(request.getDeviceId()).block();
                    assertThat(deviceOtp).isNotNull();
                    assertThat(deviceOtp.getId()).isEqualTo(oldDeviceOtp.getId());
                    assertThat(deviceOtp.getDeviceId()).isEqualTo(request.getDeviceId());
                    assertThat(deviceOtp.getSeed()).isNotEqualTo(new String(Base64.getDecoder().decode(oldDeviceOtp.getSeed())));
                });
    }
}
