package io.contactdiscovery.otp.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.contactdiscovery.otp.api.IdRef;
import io.contactdiscovery.otp.api.RegisterDeviceOtp;
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
    public Mono<IdRef> generate(@RequestBody final Mono<RegisterDeviceOtp> request) {
        return request.flatMap(deviceOtpService::register)
                .map(IdRef::new);
    }
}
