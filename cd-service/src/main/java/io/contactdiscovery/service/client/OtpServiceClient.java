package io.contactdiscovery.service.client;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.contactdiscovery.service.api.external.RegisterDeviceOtpRequest;
import io.contactdiscovery.service.api.external.RegisterDeviceOtpResponse;

/**
 * @author Mykola Yashchenko
 */
public interface OtpServiceClient {

    @RequestMapping(method = RequestMethod.POST, value = "/otps")
    RegisterDeviceOtpResponse register(RegisterDeviceOtpRequest request);
}
