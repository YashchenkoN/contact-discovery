package io.contactdiscovery.otp.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import io.contactdiscovery.otp.api.RegisterDeviceOtpRequest;
import io.contactdiscovery.otp.api.VerifyOtpRequest;
import io.contactdiscovery.otp.entity.DeviceOtp;
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
                .switchIfEmpty(repository.save(deviceOtp))
                .then(Mono.empty());
    }

    @Override
    public Mono<Void> verify(VerifyOtpRequest request) {
        return Mono.empty();
    }
}
