package io.contactdiscovery.service.api;

import javax.validation.constraints.NotEmpty;

import io.contactdiscovery.service.entity.User;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Mykola Yashchenko
 */
@Getter
@Setter
public class RegisterUserRequest {

    @NotEmpty
    private String phoneNumber;

    public User toUser() {
        final User user = new User();
        user.setPhoneNumber(phoneNumber);
        return user;
    }
}
