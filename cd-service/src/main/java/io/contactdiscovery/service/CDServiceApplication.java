package io.contactdiscovery.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CDServiceApplication {

	public static void main(String[] args) {
		System.setProperty("es.set.netty.runtime.available.processors", "0");
		SpringApplication.run(CDServiceApplication.class, args);
	}
}
