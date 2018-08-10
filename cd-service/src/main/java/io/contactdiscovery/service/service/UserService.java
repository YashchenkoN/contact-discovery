package io.contactdiscovery.service.service;

import io.contactdiscovery.service.api.RegisterUserRequest;
import io.contactdiscovery.service.api.RegisterUserResponse;
import reactor.core.publisher.Mono;

/**
 * @author Mykola Yashchenko
 */
public interface UserService {
    Mono<RegisterUserResponse> register(final RegisterUserRequest request);
}
