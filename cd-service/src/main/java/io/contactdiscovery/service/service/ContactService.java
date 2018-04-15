package io.contactdiscovery.service.service;

import io.contactdiscovery.service.api.CreateContactRequest;
import reactor.core.publisher.Mono;

public interface ContactService {
    Mono<String> create(CreateContactRequest request);
    Mono<Void> delete(String id);
}
