package io.contactdiscovery.service.service.impl;

import org.springframework.stereotype.Service;

import io.contactdiscovery.service.api.ActivateDeviceRequest;
import io.contactdiscovery.service.api.RegisterUserRequest;
import io.contactdiscovery.service.api.RegisterUserResponse;
import io.contactdiscovery.service.api.external.RegisterDeviceOtpRequest;
import io.contactdiscovery.service.api.external.VerifyOtpRequest;
import io.contactdiscovery.service.client.OtpServiceClient;
import io.contactdiscovery.service.entity.User;
import io.contactdiscovery.service.entity.UserStatus;
import io.contactdiscovery.service.exception.NotFoundException;
import io.contactdiscovery.service.exception.OtpVerificationFailed;
import io.contactdiscovery.service.repository.UserRepository;
import io.contactdiscovery.service.service.UserService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * @author Mykola Yashchenko
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final OtpServiceClient otpServiceClient;

    @Override
    public Mono<RegisterUserResponse> register(final RegisterUserRequest request) {
        final User user = request.toUser();
        user.setStatus(UserStatus.NOT_ACTIVATED);

        return userRepository.deleteByPhoneNumber(request.getPhoneNumber())
                .then(Mono.defer(() -> userRepository.save(user)))
                .map(User::getId)
                .flatMap(userId ->
                        otpServiceClient.register(new RegisterDeviceOtpRequest(request.getPhoneNumber()))
                                .thenReturn(new RegisterUserResponse(userId))
                );
    }

    @Override
    public Mono<Void> activate(final String deviceId, final ActivateDeviceRequest request) {
        return userRepository.findById(deviceId)
                .switchIfEmpty(Mono.error(new NotFoundException()))
                .flatMap(u ->
                        otpServiceClient
                                .verify(new VerifyOtpRequest(u.getPhoneNumber(), request.getOtp()))
                                .onErrorResume(ex -> Mono.error(new OtpVerificationFailed()))
                                .then(Mono.defer(() -> Mono.just(u)))
                                .map(r -> {
                                    r.setStatus(UserStatus.ACTIVATED);
                                    return r;
                                })
                                .flatMap(userRepository::save)
                                .flatMap($ -> Mono.empty())
                );
    }
}
