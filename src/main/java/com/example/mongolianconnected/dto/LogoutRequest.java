package com.example.mongolianconnected.dto;

import lombok.Data;

@Data
public class LogoutRequest {
    private String refreshToken;
}