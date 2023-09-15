package com.sixbald.webide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableJpaAuditing
public class WebideApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebideApplication.class, args);
	}

}
