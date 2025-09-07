# Spring Kafka Producer

This project is a simple **Spring Boot Kafka Producer** application. It demonstrates how to integrate **Apache Kafka** with **Spring Kafka**, configure a producer, and publish messages to a Kafka topic. The producer sends JSON payloads to Kafka, which can then be consumed by a Kafka consumer (e.g., another Spring Boot app or CLI consumer).

It’s designed for beginners learning Kafka with Spring, covering:
- Kafka producer configuration  
- Sending messages asynchronously & synchronously  
- Serializing JSON messages  
- Using REST endpoints to trigger Kafka message production  

---

## 📋 Features
- Spring Boot 3 & Spring Kafka integration  
- Kafka producer configuration  
- Publish plain strings and JSON objects  
- Expose REST endpoints to send messages  
- Asynchronous message sending with callbacks  

---

## ⚙️ Prerequisites
- **Java 21+**  
- **Maven or Gradle**  
- **Docker (for running Kafka locally)**  
- **Kafka broker** (you can start one with Docker Compose)  

Example Kafka setup with Docker Compose:

```yaml
version: '3.8'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.6.1
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
  kafka:
    image: confluentinc/cp-kafka:7.6.1
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
