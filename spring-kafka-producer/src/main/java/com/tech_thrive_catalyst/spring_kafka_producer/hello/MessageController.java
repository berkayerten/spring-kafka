package com.tech_thrive_catalyst.spring_kafka_producer.hello;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MessageController {

    private final KafkaProducerService kafkaProducerService;

    public MessageController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping("/v1/message")
    public ResponseEntity<String> sendMessage(@RequestBody Greeting greeting) throws JsonProcessingException {
        kafkaProducerService.sendJsonMessage(greeting);
        return ResponseEntity.ok("Message sent to kafka topic in Json Format");
    }

    @PostMapping("/v2/message")
    public ResponseEntity<String> sendMessageAvro(@RequestBody Greeting greeting) throws JsonProcessingException {
        kafkaProducerService.sendAvroMessage(greeting);
        return ResponseEntity.ok("Message sent to kafka topic in Avro Format");
    }
}
