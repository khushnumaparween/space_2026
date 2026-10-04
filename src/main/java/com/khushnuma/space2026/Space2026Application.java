package com.khushnuma.space2026;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class Space2026Application {

	public static void main(String[] args) {
		SpringApplication.run(Space2026Application.class, args);
	}

}

