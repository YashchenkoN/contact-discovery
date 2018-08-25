package io.contactdiscovery.otp.controller;

import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Base32;
import org.jboss.aerogear.security.otp.api.Clock;
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
import io.contactdiscovery.otp.api.VerifyOtpRequest;
import io.contactdiscovery.otp.entity.DeviceOtp;
import io.contactdiscovery.otp.repository.DeviceOtpRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

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
        deviceOtp.setSeed(Base32.encode(UUID.randomUUID().toString().getBytes()));
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
                    try {
                        assertThat(deviceOtp.getSeed()).isNotEqualTo(new String(Base32.decode(oldDeviceOtp.getSeed())));
                    } catch (final Base32.DecodingException e) {
                        fail("Decoding exception");
                    }
                });
    }

    @Test
    public void shouldVerifyOtp() {
        final String deviceId = RandomStringUtils.randomNumeric(10);

        final DeviceOtp deviceOtp = deviceOtp(deviceId);
        deviceOtpRepository.save(deviceOtp).block();

        final VerifyOtpRequest request = new VerifyOtpRequest();
        request.setDeviceId(deviceId);
        request.setOtp(new Totp(deviceOtp.getSeed(), new Clock(60)).now());

        webTestClient.post()
                .uri("/otps/verify")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(request))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void shouldReturnErrorOnVerifyIfDeviceNotFound() {
        final String deviceId = RandomStringUtils.randomNumeric(10);

        final VerifyOtpRequest request = new VerifyOtpRequest();
        request.setDeviceId(deviceId);
        request.setOtp(UUID.randomUUID().toString());

        webTestClient.post()
                .uri("/otps/verify")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(request))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void shouldReturnErrorOnVerifyIfOtpIsIncorrect() {
        final String deviceId = RandomStringUtils.randomNumeric(10);

        final DeviceOtp deviceOtp = deviceOtp(deviceId);
        deviceOtpRepository.save(deviceOtp).block();

        final VerifyOtpRequest request = new VerifyOtpRequest();
        request.setDeviceId(deviceId);
        request.setOtp("123467");

        webTestClient.post()
                .uri("/otps/verify")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(request))
                .exchange()
                .expectStatus().isForbidden();
    }
}
