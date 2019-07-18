package com.raul.mongodelivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication
public class MongoDeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MongoDeliveryApplication.class, args);
	}

}
