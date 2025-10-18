package com.menkaix.smartlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
		"com.menkaix.smartlog.controller",
		"com.menkaix.smartlog.service"
})
@EnableJpaRepositories("com.menkaix.smartlog.repository")
@EntityScan("com.menkaix.smartlog.model")
public class SmartlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartlogApplication.class, args);
	}

}
