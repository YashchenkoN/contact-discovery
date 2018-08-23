package io.contactdiscovery.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Mykola Yashchenko
 */
public class OtpVerificationFailed extends ResponseStatusException {
    public OtpVerificationFailed() {
        super(HttpStatus.BAD_REQUEST);
    }
}
