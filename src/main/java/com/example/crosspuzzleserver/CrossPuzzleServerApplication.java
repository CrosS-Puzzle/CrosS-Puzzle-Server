package com.example.crosspuzzleserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class CrossPuzzleServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrossPuzzleServerApplication.class, args);
	}

}
