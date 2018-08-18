package io.contactdiscovery.otp.api;

import lombok.Data;

/**
 * @author Mykola Yashchenko
 */
@Data
public class VerifyOtpRequest {
    private String deviceId;
    private String otp;
}
