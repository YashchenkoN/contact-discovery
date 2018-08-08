package io.contactdiscovery.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.contactdiscovery.service.client.OtpServiceClient;
import reactivefeign.ReactiveFeign;

/**
 * @author Mykola Yashchenko
 */
@Configuration
public class ReactiveFeignConfig {

    @Bean
    public OtpServiceClient otpServiceClient() {
        return ReactiveFeign
                .<OtpServiceClient>builder()
                .target(OtpServiceClient.class, "http://cd-otp-service");
    }
}
