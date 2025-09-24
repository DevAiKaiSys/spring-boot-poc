package com.example.websocketredis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisMessageSubscriber implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    // 💡 เราไม่จำเป็นต้องใช้ ObjectMapper ที่นี่แล้ว สามารถลบออกได้
    public RedisMessageSubscriber(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        try {
            String topic = new String(message.getChannel());
            // 1. อ่านเนื้อหาของ message ซึ่งเป็น JSON String
            String jsonMessage = new String(message.getBody());

            System.out.println("Received raw JSON from Redis, forwarding to WebSocket topic '" + topic + "': " + jsonMessage);

            // 💡 2. ส่ง JSON String ดิบๆ ไปยัง WebSocket topic โดยตรง
            //    (ไม่ต้องแปลงเป็น Object ก่อน)
            messagingTemplate.convertAndSend(topic, jsonMessage);

        } catch (Exception e) {
            // ควรใช้ Logger แทน System.err.println ใน Production
            System.err.println("Could not process and forward Redis message: " + e.getMessage());
        }
    }
}