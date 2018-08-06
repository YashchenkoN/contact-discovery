package io.contactdiscovery.service.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import io.contactdiscovery.service.entity.User;
import reactor.core.publisher.Mono;

/**
 * @author Mykola Yashchenko
 */
public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findByPhoneNumber(String phoneNumber);
    Mono<User> deleteByPhoneNumber(String phoneNumber);
}
