package com.example.mongolianconnected.dto;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String email;      // Email login
    private String password;   // Raw password
}