package io.contactdiscovery.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ContactDiscoveryApplication {

	public static void main(String[] args) {
		System.setProperty("es.set.netty.runtime.available.processors", "0");
		SpringApplication.run(ContactDiscoveryApplication.class, args);
	}
}
