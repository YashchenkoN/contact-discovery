package io.contactdiscovery.query;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CDQueryApplication {

	public static void main(String[] args) {
		System.setProperty("es.set.netty.runtime.available.processors", "0");
		SpringApplication.run(CDQueryApplication.class, args);
	}
}
