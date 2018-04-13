package io.contactdiscovery.service.api;

import javax.validation.constraints.NotEmpty;

import io.contactdiscovery.service.entity.Contact;

/**
 * @author Mykola Yashchenko
 */
public class CreateContactRequest {
    @NotEmpty
    private String number;
    @NotEmpty
    private String fistName;
    @NotEmpty
    private String lastName;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getFistName() {
        return fistName;
    }

    public void setFistName(String fistName) {
        this.fistName = fistName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Contact toContact() {
        final Contact contact = new Contact();
        contact.setNumber(number);
        contact.setFistName(fistName);
        contact.setLastName(lastName);
        contact.setFullName(fistName + " " + lastName);
        return contact;
    }
}
