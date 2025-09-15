package com.tech_thrive_catalyst.spring_kafka_producer.hello;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerFormatConfig {

    private static final String SCHEMA_REGISTRY_URL = "schema.registry.url";

    @Value("${kafka.avro.value-serializer}")
    private String avroSerializer;

    @Value("${kafka.json.value-serializer}")
    private String jsonSerializer;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.producer.properties.schema.registry.url}")
    private String schemaRegistryUrl;

    @Bean
    public KafkaTemplate<String, Object> jsonSchemaKafkaTemplate() {
        return new KafkaTemplate<>(jsonSchemaProducerFactory());
    }

    @Bean
    public ProducerFactory<String, Object> jsonSchemaProducerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, jsonSerializer);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, Object> avroSchemaKafkaTemplate(KafkaTemplate<String, Object> avroSchemaKafkaTemplate) {
        return new KafkaTemplate<>(avroSchemaProducerFactory());
    }

    @Bean
    public ProducerFactory<String, Object> avroSchemaProducerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(SCHEMA_REGISTRY_URL, schemaRegistryUrl);

        return new DefaultKafkaProducerFactory<>(config);
    }

}
