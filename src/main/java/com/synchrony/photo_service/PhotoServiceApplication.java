package com.synchrony.photo_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.synchrony.photo_service.services.imgur")
@EnableJpaRepositories(basePackages = "com.synchrony.photo_service.repos")
@EnableJpaAuditing
public class PhotoServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(PhotoServiceApplication.class, args);
	}
}
