package com.sparta.myvoyageblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MyVoyageBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyVoyageBlogApplication.class, args);
	}

}
