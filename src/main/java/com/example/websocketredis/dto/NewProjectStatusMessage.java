package com.example.websocketredis.dto;

import java.io.Serializable;

public record NewProjectStatusMessage(String taskId, NewProjectStatus status, String message) implements Serializable {
}

