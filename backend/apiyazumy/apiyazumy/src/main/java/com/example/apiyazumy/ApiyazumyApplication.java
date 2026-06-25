package com.example.apiyazumy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.example.apiyazumy")
@EnableJpaRepositories(basePackages = "com.example.apiyazumy.repository")
@EntityScan(basePackages = "com.example.apiyazumy.entity")
public class ApiyazumyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiyazumyApplication.class, args);
	}

}
