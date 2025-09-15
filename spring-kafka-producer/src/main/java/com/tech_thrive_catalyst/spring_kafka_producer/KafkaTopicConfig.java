package com.tech_thrive_catalyst.spring_kafka_producer;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {

    public static final String GREETINGS_JSON_TOPIC = "greetings-json";
    public static final String GREETINGS_AVRO_TOPIC = "greetings-avro";

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> confiigs = new HashMap<>();
        confiigs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(confiigs);
    }

    @Bean
    public NewTopic greetingsJsonTopic() {
        return new NewTopic(GREETINGS_JSON_TOPIC, 3, (short) 2);
    }

    @Bean
    public NewTopic greetingsAvroTopic() {
        return new NewTopic(KafkaTopicConfig.GREETINGS_AVRO_TOPIC, 3, (short) 2);
    }
}
