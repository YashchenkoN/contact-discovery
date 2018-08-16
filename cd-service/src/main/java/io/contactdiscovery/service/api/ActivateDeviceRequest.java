package io.contactdiscovery.service.api;

import lombok.Data;

/**
 * @author Mykola Yashchenko
 */
@Data
public class ActivateDeviceRequest {
    private String otp;
}
