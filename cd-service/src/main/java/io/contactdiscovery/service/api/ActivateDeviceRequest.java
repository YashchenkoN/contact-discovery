package io.contactdiscovery.service.api;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

/**
 * @author Mykola Yashchenko
 */
@Data
public class ActivateDeviceRequest {
    @NotEmpty
    private String otp;
}
