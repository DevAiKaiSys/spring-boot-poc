package com.example.websocketredis.controller;

import com.example.websocketredis.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/new-project")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
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
}