package io.contactdiscovery.query;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "io.contactdiscovery.query.repository")
public class ContactDiscoveryQueryApplication {

	public static void main(String[] args) {
		System.setProperty("es.set.netty.runtime.available.processors", "0");
		SpringApplication.run(ContactDiscoveryQueryApplication.class, args);
	}
}
