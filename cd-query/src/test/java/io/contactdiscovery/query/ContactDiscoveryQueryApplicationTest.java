package io.contactdiscovery.query;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.contactdiscovery.extension.ElasticsearchDockerExtension;

/**
 * @author Mykola Yashchenko
 */
@ExtendWith({ElasticsearchDockerExtension.class, SpringExtension.class})
@SpringBootTest(classes = ContactDiscoveryQueryApplication.class)
public class ContactDiscoveryQueryApplicationTest {

    @Test
    public void shouldLoadContext() {

    }
}
