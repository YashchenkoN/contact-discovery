package io.contactdiscovery.service.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import io.contactdiscovery.service.entity.User;

/**
 * @author Mykola Yashchenko
 */
public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
