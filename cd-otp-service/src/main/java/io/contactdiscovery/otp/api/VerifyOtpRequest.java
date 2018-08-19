package io.contactdiscovery.otp.api;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

/**
 * @author Mykola Yashchenko
 */
@Data
public class VerifyOtpRequest {
    @NotEmpty
    private String deviceId;

    @NotEmpty
    private String otp;
}
