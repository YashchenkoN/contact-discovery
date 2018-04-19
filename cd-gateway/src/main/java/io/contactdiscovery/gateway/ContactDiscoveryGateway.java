package io.contactdiscovery.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author Mykola Yashchenko
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class ContactDiscoveryGateway {

    public static void main(final String[] args) {
        SpringApplication.run(ContactDiscoveryGateway.class, args);
    }
}
