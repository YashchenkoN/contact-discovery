package io.contactdiscovery.service.client;

import feign.Headers;
import feign.RequestLine;
import io.contactdiscovery.service.api.external.RegisterDeviceOtpRequest;
import reactor.core.publisher.Mono;

/**
 * @author Mykola Yashchenko
 */
@Headers({ "Accept: application/json" })
public interface OtpServiceClient {

    @RequestLine("POST /otps")
    @Headers("Content-Type: application/json")
    Mono<Void> register(RegisterDeviceOtpRequest request);
}
