package io.contactdiscovery.service.api;

import javax.validation.constraints.NotEmpty;

import io.contactdiscovery.service.entity.Contact;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Mykola Yashchenko
 */
@Getter
@Setter
public class CreateContactRequest {
    @NotEmpty
    private String number;
    @NotEmpty
    private String fistName;
    @NotEmpty
    private String lastName;

    public Contact toContact() {
        final Contact contact = new Contact();
        contact.setNumber(number);
        contact.setFistName(fistName);
        contact.setLastName(lastName);
        contact.setFullName(fistName + " " + lastName);
        return contact;
    }
}
