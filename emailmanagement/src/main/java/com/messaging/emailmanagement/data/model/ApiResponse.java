package com.messaging.emailmanagement.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiResponse<T> {
    private int status;
    private String message;
    private String sessionId;
}
