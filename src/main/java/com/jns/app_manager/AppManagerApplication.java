package com.jns.app_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AppManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppManagerApplication.class, args);
	}

}
