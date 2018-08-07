package io.contactdiscovery.service.api.external;

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
public class RegisterDeviceOtpResponse {
    private String seed;
}
