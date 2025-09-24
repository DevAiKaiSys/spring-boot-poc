package com.example.websocketredis.controller;


import com.example.websocketredis.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// ระบุว่านี่คือเทสต์สำหรับ Controller ชื่อ ProjectController
@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
    }

    @Test
    void whenProjectExists_shouldReturnConflict() throws Exception {
        // Arrange
        String customer = "testCustomer";
        String projectName = "testProject";
        when(projectService.checkExists(customer, projectName)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/new-project/checkExists")
                        .param("customer", customer)
                        .param("projectName", projectName))
                .andExpect(status().isConflict()) // 1. ตรวจสอบว่า Status Code เป็น 409 Conflict
                .andExpect(jsonPath("$.message", is("Project name '" + projectName + "' is already taken."))); // 2. ตรวจสอบข้อความใน body
    }

    @Test
    void whenProjectDoesNotExist_shouldReturnOk() throws Exception {
        // Arrange
        String customer = "testCustomer";
        String projectName = "nonExistentProject";
        when(projectService.checkExists(customer, projectName)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/api/new-project/checkExists")
                        .param("customer", customer)
                        .param("projectName", projectName))
                .andExpect(status().isOk()) // 3. ตรวจสอบว่า Status Code เป็น 200 OK
                .andExpect(content().string("")); // 4. ตรวจสอบว่า Response Body ว่างเปล่า
    }
}