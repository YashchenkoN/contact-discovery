package io.contactdiscovery.otp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

/**
 * @author Mykola Yashchenko
 */
public class OtpVerificationException extends HttpStatusCodeException {

    public OtpVerificationException() {
        super(HttpStatus.FORBIDDEN);
    }
}
