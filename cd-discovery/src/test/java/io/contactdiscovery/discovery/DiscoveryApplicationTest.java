package io.contactdiscovery.discovery;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Mykola Yashchenko
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DiscoveryApplication.class)
public class DiscoveryApplicationTest {

    @Test
    public void shouldLoadContext() {

    }
}
