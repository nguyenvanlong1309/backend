package com.tuthien.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableAsync
public class BackEndApplication {

	@Bean
	public PasswordEncoder delegatingPasswordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(BackEndApplication.class, args);
	}

}
