package io.contactdiscovery.otp.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Mykola Yashchenko
 */
@Getter
@Setter
@Document(collection = "otps")
public class DeviceOtp {
    @Id
    private String id;
    private String deviceId;
    private String seed;
}
