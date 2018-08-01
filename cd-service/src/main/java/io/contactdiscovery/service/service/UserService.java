package io.contactdiscovery.service.service;

import io.contactdiscovery.service.api.RegisterUserRequest;
import reactor.core.publisher.Mono;

/**
 * @author Mykola Yashchenko
 */
public interface UserService {
    Mono<String> register(final RegisterUserRequest request);
}
