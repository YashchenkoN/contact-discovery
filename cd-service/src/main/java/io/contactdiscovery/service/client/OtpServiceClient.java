package io.contactdiscovery.service.client;

import feign.RequestLine;
import io.contactdiscovery.service.api.external.RegisterDeviceOtpRequest;
import io.contactdiscovery.service.api.external.RegisterDeviceOtpResponse;
import reactor.core.publisher.Mono;

/**
 * @author Mykola Yashchenko
 */
public interface OtpServiceClient {

    @RequestLine("POST /otps")
    Mono<RegisterDeviceOtpResponse> register(RegisterDeviceOtpRequest request);
}
