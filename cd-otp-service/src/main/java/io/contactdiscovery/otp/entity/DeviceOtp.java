package io.contactdiscovery.otp.entity;

import java.util.Base64;

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

    public String getEncodedSeed() {
        return Base64.getEncoder().encodeToString(seed.getBytes());
    }
}
