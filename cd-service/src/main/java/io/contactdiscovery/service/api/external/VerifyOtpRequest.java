package io.contactdiscovery.service.api.external;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Mykola Yashchenko
 */
@Data
@AllArgsConstructor
public class VerifyOtpRequest {
    private String deviceId;
    private String otp;
}
