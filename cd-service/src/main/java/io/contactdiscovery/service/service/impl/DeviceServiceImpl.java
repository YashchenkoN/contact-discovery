package io.contactdiscovery.service.service.impl;

import org.springframework.stereotype.Service;

import io.contactdiscovery.service.api.RegisterUserRequest;
import io.contactdiscovery.service.entity.User;
import io.contactdiscovery.service.entity.UserStatus;
import io.contactdiscovery.service.repository.UserRepository;
import io.contactdiscovery.service.service.UserService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * @author Mykola Yashchenko
 */
@Service
@AllArgsConstructor
public class DeviceServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public Mono<String> register(final RegisterUserRequest request) {
        final User user = request.toUser();
        user.setStatus(UserStatus.NOT_ACTIVATED);

        return userRepository.deleteByPhoneNumber(request.getPhoneNumber())
                .then(userRepository.save(user))
                .map(User::getId);
    }
}
