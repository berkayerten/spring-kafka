package com.tech_thrive_catalyst.spring_kafka_producer;

import org.springframework.boot.SpringApplication;

public class TestSpringKafkaProducerApplication {

	public static void main(String[] args) {
		SpringApplication.from(SpringKafkaProducerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
