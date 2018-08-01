package io.contactdiscovery.service.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Mykola Yashchenko
 */
@Getter
@Setter
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String phoneNumber;
    private UserStatus status;
}
