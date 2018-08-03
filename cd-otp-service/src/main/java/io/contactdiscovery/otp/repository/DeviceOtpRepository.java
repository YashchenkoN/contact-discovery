package io.contactdiscovery.otp.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import io.contactdiscovery.otp.entity.DeviceOtp;
import reactor.core.publisher.Mono;

/**
 * @author Mykola Yashchenko
 */
public interface DeviceOtpRepository extends ReactiveMongoRepository<DeviceOtp, String> {
    Mono<DeviceOtp> findByDeviceId(String deviceId);
}
