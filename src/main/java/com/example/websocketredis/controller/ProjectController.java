package com.example.websocketredis.controller;

import com.example.websocketredis.dto.StartProjectRequest;
import com.example.websocketredis.service.FileUploadService;
import com.example.websocketredis.service.ProjectCreationService;
import com.example.websocketredis.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/new-project")
public class ProjectController {

    private final ProjectService projectService;
    private final FileUploadService fileUploadService;
    private final ProjectCreationService projectCreationService;

    public ProjectController(ProjectService projectService, FileUploadService fileUploadService, ProjectCreationService projectCreationService) {
        this.projectService = projectService;
        this.fileUploadService = fileUploadService;
        this.projectCreationService = projectCreationService;
    }

    @GetMapping("/checkExists")
    // 1. เปลี่ยน return type เป็น ResponseEntity
    public ResponseEntity<?> checkExists(
            @RequestParam String customer,
            @RequestParam String projectName) {

        boolean exists = projectService.checkExists(customer, projectName);

        if (exists) {
            // 2. ถ้ามีโปรเจกต์อยู่แล้ว, ส่ง status 409 Conflict พร้อมข้อความ
            Map<String, String> responseBody = Map.of("message", "Project name '" + projectName + "' is already taken.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
        } else {
            // 3. ถ้าชื่อว่าง, ส่ง status 200 OK และ body ว่าง
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        String taskId = UUID.randomUUID().toString();
        fileUploadService.storeFiles(taskId, files);
        return ResponseEntity.ok(Map.of("taskId", taskId));
    }

    @PostMapping("/start")
    public ResponseEntity<Object> startNewProject(@RequestBody StartProjectRequest request) {
        System.out.println("Starting project creation for task ID: " + request.getTaskId());

        // --- FIX: Pass the entire 'request' object to the service ---
        projectCreationService.startProjectCreation(request);

        return ResponseEntity.ok(Map.of("message", "Project creation process started."));
    }
}