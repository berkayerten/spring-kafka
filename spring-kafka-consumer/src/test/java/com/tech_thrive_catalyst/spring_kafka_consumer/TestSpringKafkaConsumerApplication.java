package com.tech_thrive_catalyst.spring_kafka_consumer;

import org.springframework.boot.SpringApplication;

public class TestSpringKafkaConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.from(SpringKafkaConsumerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
