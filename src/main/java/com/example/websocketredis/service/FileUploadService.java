package com.example.websocketredis.service;

import com.example.websocketredis.dto.NewProjectStatus;
import com.example.websocketredis.dto.NewProjectStatusMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {

    private final RedisPublisherService publisher;

    public FileUploadService(RedisPublisherService publisher) {
        this.publisher = publisher;
        // ... file storage setup
    }

    @Async
    public void storeFiles(String taskId, MultipartFile[] files) {
        final String topic = "/topic/new-project-status/" + taskId;
        try {
            publisher.publishAsJson(topic, new NewProjectStatusMessage(taskId, NewProjectStatus.UPLOADING, "Starting file upload..."));
            // ... your file upload logic ...
            publisher.publishAsJson(topic, new NewProjectStatusMessage(taskId, NewProjectStatus.UPLOAD_COMPLETE, "All files uploaded successfully."));
        } catch (Exception e) {
            publisher.publishAsJson(topic, new NewProjectStatusMessage(taskId, NewProjectStatus.UPLOAD_FAILED, "File upload failed."));
        }
    }
}