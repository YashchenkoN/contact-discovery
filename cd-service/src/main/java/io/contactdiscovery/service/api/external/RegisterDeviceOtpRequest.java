package io.contactdiscovery.service.api.external;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Mykola Yashchenko
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDeviceOtpRequest {
    @NotEmpty
    private String deviceId;
}
