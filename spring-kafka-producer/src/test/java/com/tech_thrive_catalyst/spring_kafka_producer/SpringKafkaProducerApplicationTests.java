package com.tech_thrive_catalyst.spring_kafka_producer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class SpringKafkaProducerApplicationTests {

	@Test
	void contextLoads() {
	}

}
