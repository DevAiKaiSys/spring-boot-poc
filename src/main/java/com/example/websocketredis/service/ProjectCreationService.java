package com.example.websocketredis.service;

import com.example.websocketredis.dto.NewProjectStatus;
import com.example.websocketredis.dto.NewProjectStatusMessage;
import com.example.websocketredis.dto.StartProjectRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ProjectCreationService {

    private final RedisPublisherService publisher;

    public ProjectCreationService(RedisPublisherService publisher) {
        this.publisher = publisher;
    }

    @Async
    public void startProjectCreation(StartProjectRequest request) {
        String taskId = request.getTaskId();
        final String topic = "/topic/new-project-status/" + taskId;
        try {
            // --- FIX 2: Use the publisher to send messages to Redis ---
            publisher.publishAsJson(topic, new NewProjectStatusMessage(taskId, NewProjectStatus.IN_PROGRESS, "Step 1/3: Validating project files..."));
            Thread.sleep(3000);

            publisher.publishAsJson(topic, new NewProjectStatusMessage(taskId, NewProjectStatus.IN_PROGRESS, "Step 2/3: Setting up project structure..."));
            Thread.sleep(4000);

            publisher.publishAsJson(topic, new NewProjectStatusMessage(taskId, NewProjectStatus.IN_PROGRESS, "Step 3/3: Finalizing project..."));
            Thread.sleep(2000);

            publisher.publishAsJson(topic, new NewProjectStatusMessage(taskId, NewProjectStatus.COMPLETED, "Project created successfully!"));
        } catch (InterruptedException e) {
            publisher.publishAsJson(topic, new NewProjectStatusMessage(taskId, NewProjectStatus.FAILED, "Project creation was interrupted."));
            Thread.currentThread().interrupt();
        }
    }
}