package io.contactdiscovery.otp.api;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Mykola Yashchenko
 */
@Getter
@Setter
public class RegisterDeviceOtpRequest {
    @NotEmpty
    private String deviceId;
}
