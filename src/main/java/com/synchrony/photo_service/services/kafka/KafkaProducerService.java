package com.synchrony.photo_service.services.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "image-uploads";

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String username, String imageUrl) {
        logger.debug("Sending message to topic for {} with image URL: {}", username, imageUrl);
        Map<String, String> message = new HashMap();
        message.put("userName", username);
        message.put("imageUrl", imageUrl);
        message.put("timestamp", String.valueOf(System.currentTimeMillis()));

        try {
            kafkaTemplate.send(TOPIC, new ObjectMapper().writeValueAsString(message));
            logger.debug("Message sent to Kafka: {}", message);
        } catch (Exception e) {
            logger.error("Failed to send message to Kafka", e);
        }
    }
}
