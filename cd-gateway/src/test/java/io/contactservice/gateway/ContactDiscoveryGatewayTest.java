package io.contactservice.gateway;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.contactdiscovery.gateway.ContactDiscoveryGateway;

/**
 * @author Mykola Yashchenko
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ContactDiscoveryGateway.class)
public class ContactDiscoveryGatewayTest {

    @Test
    public void shouldLoadContext() {

    }
}
