package com.example.mongolianconnected.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
// Login Response DTO
public class UserLoginResponse {
    private UserDto user;
    private List<String> permissions;
    private String accessToken;
    private String refreshToken;
}
