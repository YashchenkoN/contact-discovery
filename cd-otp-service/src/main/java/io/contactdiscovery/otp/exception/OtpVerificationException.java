package io.contactdiscovery.otp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Mykola Yashchenko
 */
public class OtpVerificationException extends ResponseStatusException {

    public OtpVerificationException() {
        super(HttpStatus.FORBIDDEN);
    }
}
