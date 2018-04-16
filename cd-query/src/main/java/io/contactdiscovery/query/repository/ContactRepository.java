package io.contactdiscovery.query.repository;

import java.util.stream.Stream;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import io.contactdiscovery.query.entity.Contact;

/**
 * @author Mykola Yashchenko
 */
public interface ContactRepository extends ElasticsearchRepository<Contact, String> {
    Stream<Contact> findByFullName(String fullName, Pageable pageable);
}
