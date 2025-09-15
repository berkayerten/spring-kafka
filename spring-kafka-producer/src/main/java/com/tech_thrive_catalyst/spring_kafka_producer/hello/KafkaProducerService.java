package com.tech_thrive_catalyst.spring_kafka_producer.hello;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import com.tech_thrive_catalyst.avro.GreetingEvent;
import com.tech_thrive_catalyst.spring_kafka_producer.KafkaTopicConfig;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> jsonSchemaKafkaTemplate;
    private final KafkaTemplate<String, Object> avroSchemaKafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonSchema schema;

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerService.class);

    public KafkaProducerService(
            KafkaTemplate<String, Object> jsonSchemaKafkaTemplate,
            KafkaTemplate<String, Object> avroSchemaKafkaTemplate) {
        this.jsonSchemaKafkaTemplate = jsonSchemaKafkaTemplate;
        this.avroSchemaKafkaTemplate = avroSchemaKafkaTemplate;
        this.schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7)
                .getSchema(getClass().getResourceAsStream("/kafka/schema/json/greetings-schema.json"));
    }

    public void sendJsonMessage(Greeting greeting) throws JsonProcessingException {
        String json = convertGreetingJson(greeting);
        jsonSchemaKafkaTemplate.send(KafkaTopicConfig.GREETINGS_JSON_TOPIC, json);
        LOGGER.info("Produced json message name -> {}, message body -> {}", greeting.name(), greeting.message());
    }

    public void sendAvroMessage(Greeting greeting) {
        GreetingEvent avro = convertGreetingAvro(greeting);
        avroSchemaKafkaTemplate.send(KafkaTopicConfig.GREETINGS_AVRO_TOPIC, greeting.name(), avro);
        LOGGER.info("Produced avro message name -> {}, message body -> {}, with emoji -> {}",
                greeting.name(), greeting.message(), greeting.emoji());
    }

    private GreetingEvent convertGreetingAvro(Greeting greeting) {
        return new GreetingEvent(greeting.name(), greeting.message(), greeting.emoji());
    }

    private String convertGreetingJson(Greeting greeting) throws JsonProcessingException {
        Map<String, Object> payload = Map.of("name", greeting.name(), "message", greeting.message());
        String json = objectMapper.writeValueAsString(payload);
        validateSchema(json);

        return json;
    }

    private void validateSchema(String json) throws JsonProcessingException {
        Set<ValidationMessage> errors = schema.validate(objectMapper.readTree(json));
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Invalid greeting" + errors);
        }
    }
}
