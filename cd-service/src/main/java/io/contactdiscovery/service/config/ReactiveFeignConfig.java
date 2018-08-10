package io.contactdiscovery.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.contactdiscovery.service.client.OtpServiceClient;
import reactivefeign.ReactiveFeign;

/**
 * @author Mykola Yashchenko
 */
@Configuration
public class ReactiveFeignConfig {

    @Value("${service.otp-service-host}")
    private String otpServiceHost;

    @Bean
    public OtpServiceClient otpServiceClient() {
        return ReactiveFeign
                .<OtpServiceClient>builder()
                .target(OtpServiceClient.class, otpServiceHost);
    }
}
