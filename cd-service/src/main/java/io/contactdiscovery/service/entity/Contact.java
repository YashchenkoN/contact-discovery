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
@Document(collection = "contacts")
public class Contact {
    @Id
    private String id;
    private String number;
    private String fistName;
    private String lastName;
    private String fullName;
}
