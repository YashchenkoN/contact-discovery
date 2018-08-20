package io.contactdiscovery.otp.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Clock;
import org.springframework.stereotype.Service;

import io.contactdiscovery.otp.api.RegisterDeviceOtpRequest;
import io.contactdiscovery.otp.api.VerifyOtpRequest;
import io.contactdiscovery.otp.entity.DeviceOtp;
import io.contactdiscovery.otp.exception.NotFoundException;
import io.contactdiscovery.otp.exception.OtpVerificationException;
import io.contactdiscovery.otp.repository.DeviceOtpRepository;
import io.contactdiscovery.otp.service.DeviceOtpService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * @author Mykola Yashchenko
 */
@Service
@AllArgsConstructor
public class DeviceOtpServiceImpl implements DeviceOtpService {

    private static final int SEED_LENGTH = 40;
    private static final int PERIOD = 60;

    private final DeviceOtpRepository repository;

    @Override
    public Mono<Void> register(final RegisterDeviceOtpRequest request) {
        final DeviceOtp deviceOtp = new DeviceOtp();
        deviceOtp.setDeviceId(request.getDeviceId());
        deviceOtp.setSeed(RandomStringUtils.random(SEED_LENGTH));

        return repository.findByDeviceId(request.getDeviceId())
                .flatMap(d -> {
                    d.setSeed(RandomStringUtils.random(SEED_LENGTH));
                    return repository.save(d);
                })
                .switchIfEmpty(Mono.defer(() -> repository.save(deviceOtp)))
                .then(Mono.empty());
    }

    @Override
    public Mono<Void> verify(final VerifyOtpRequest request) {
        return repository.findByDeviceId(request.getDeviceId())
                .switchIfEmpty(Mono.error(new NotFoundException()))
                .flatMap(d -> {
                    final boolean otpCorrect = new Totp(d.getSeed(), new Clock(PERIOD))
                            .verify(request.getOtp());
                    if (otpCorrect) {
                        return Mono.empty();
                    }

                    return Mono.error(new OtpVerificationException());
                });
    }
}
