package io.contactdiscovery.otp.service;

import io.contactdiscovery.otp.api.RegisterDeviceOtpRequest;
import reactor.core.publisher.Mono;

/**
 * @author Mykola Yashchenko
 */
public interface DeviceOtpService {
    Mono<String> register(RegisterDeviceOtpRequest request);
}
