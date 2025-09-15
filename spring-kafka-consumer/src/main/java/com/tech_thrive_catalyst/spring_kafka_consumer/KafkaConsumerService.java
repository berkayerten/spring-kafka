package com.tech_thrive_catalyst.spring_kafka_consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import com.tech_thrive_catalyst.avro.GreetingEvent;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    public static final String GREETINGS_JSON_TOPIC = "greetings-json";
    public static final String GREETINGS_AVRO_TOPIC = "greetings-avro";

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerService.class);

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonSchema schema;

    public KafkaConsumerService() {
        this.schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7)
                .getSchema(getClass().getResourceAsStream("/kafka/schema/json/greetings-schema.json"));
    }

    @KafkaListener(topics = GREETINGS_JSON_TOPIC,
                   groupId = "my-group",
                   containerFactory = "jsonKafkaListenerFactory")
    public void consumeJson(String message) throws JsonProcessingException {
        Greeting greeting = convertGreetingJson(message);
        LOGGER.info("Consumed Json message name -> {}, message body -> {}", greeting.name(), greeting.message());
    }


    @KafkaListener(topics = GREETINGS_AVRO_TOPIC,
                   groupId = "my-group",
                   containerFactory = "avroKafkaListenerFactory")
    public void consume(GreetingEvent message) {
        Greeting greeting = convertGreetingAvro(message);
        LOGGER.info("Consumed Avro message name -> {}, message body -> {}", greeting.name(), greeting.message());
    }

    private Greeting convertGreetingAvro(GreetingEvent message) {
        return new Greeting(message.getName(), message.getMessage());
    }

    private Greeting convertGreetingJson(String message) throws JsonProcessingException {
        validateSchema(message);
        return objectMapper.readValue(message, Greeting.class);
    }

    private void validateSchema(String message) throws JsonProcessingException {
        Set<ValidationMessage> errors = schema.validate(objectMapper.readTree(message));
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Invalid schema");
        }
    }
}
