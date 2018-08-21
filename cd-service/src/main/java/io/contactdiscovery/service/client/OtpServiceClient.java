package io.contactdiscovery.service.client;

import feign.Headers;
import feign.RequestLine;
import io.contactdiscovery.service.api.external.RegisterDeviceOtpRequest;
import io.contactdiscovery.service.api.external.VerifyOtpRequest;
import reactor.core.publisher.Mono;

/**
 * @author Mykola Yashchenko
 */
@Headers({ "Accept: application/json" })
public interface OtpServiceClient {

    @RequestLine("POST /otps")
    @Headers("Content-Type: application/json")
    Mono<Void> register(RegisterDeviceOtpRequest request);

    @RequestLine("POST /otps/verify")
    @Headers("Content-Type: application/json")
    Mono<Void> verify(VerifyOtpRequest request);
}
