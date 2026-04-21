package com.example.mongolianconnected.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;   // true = success, false = error
    private String message;    // user-friendly message
    private T data;            // payload, can be null for errors
    private String statusCode;  // optional, internal error code
}