package io.contactdiscovery.otp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

/**
 * @author Mykola Yashchenko
 */
public class NotFoundException extends HttpStatusCodeException {

    public NotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }
}
