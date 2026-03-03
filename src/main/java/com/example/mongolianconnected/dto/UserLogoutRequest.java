package com.example.mongolianconnected.dto;

import lombok.Data;

@Data
public class UserLogoutRequest {
    private String refreshToken;
}