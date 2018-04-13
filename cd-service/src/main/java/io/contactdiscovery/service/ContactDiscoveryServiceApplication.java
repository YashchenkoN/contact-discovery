package io.contactdiscovery.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ContactDiscoveryServiceApplication {

	public static void main(String[] args) {
		System.setProperty("es.set.netty.runtime.available.processors", "0");
		SpringApplication.run(ContactDiscoveryServiceApplication.class, args);
	}
}
