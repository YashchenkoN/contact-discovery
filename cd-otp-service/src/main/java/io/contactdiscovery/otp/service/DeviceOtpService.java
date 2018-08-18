package io.contactdiscovery.otp.service;

import io.contactdiscovery.otp.api.RegisterDeviceOtpRequest;
import io.contactdiscovery.otp.api.VerifyOtpRequest;
import reactor.core.publisher.Mono;

/**
 * @author Mykola Yashchenko
 */
public interface DeviceOtpService {
    Mono<Void> register(RegisterDeviceOtpRequest request);
    Mono<Void> verify(VerifyOtpRequest request);
}
