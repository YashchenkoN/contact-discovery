package io.contactdiscovery.otp.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.contactdiscovery.otp.api.RegisterDeviceOtpRequest;
import io.contactdiscovery.otp.service.DeviceOtpService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * @author Mykola Yashchenko
 */
@RestController
@AllArgsConstructor
@RequestMapping("/otps")
public class DeviceOtpController {

    private final DeviceOtpService deviceOtpService;

    @PostMapping
    public Mono<Void> register(@RequestBody final Mono<RegisterDeviceOtpRequest> request) {
        return request.flatMap(deviceOtpService::register);
    }
}
