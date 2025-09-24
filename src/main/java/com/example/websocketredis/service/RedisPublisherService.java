package com.example.websocketredis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisPublisherService {
    private static final Logger log = LoggerFactory.getLogger(RedisPublisherService.class);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisPublisherService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Publishes a raw string message directly to a Redis topic.
     *
     * @param topic   The topic to publish to.
     * @param message The raw string message.
     */
    public void publishString(String topic, String message) {
        log.info("Publishing string message: '{}' to topic: '{}'", message, topic);
        redisTemplate.convertAndSend(topic, message);
    }

    /**
     * Serializes an object to a JSON string and publishes it to a Redis topic.
     *
     * @param topic   The topic to publish to.
     * @param payload The object to be serialized and published.
     */
    public void publishAsJson(String topic, Object payload) {
        log.info("Publishing object payload as JSON to topic '{}'", topic);
        try {
            String message = objectMapper.writeValueAsString(payload);
            publishString(topic, message);
        } catch (JsonProcessingException e) {
            log.error("Could not serialize object to JSON for topic '{}'", topic, e);
        }
    }
}