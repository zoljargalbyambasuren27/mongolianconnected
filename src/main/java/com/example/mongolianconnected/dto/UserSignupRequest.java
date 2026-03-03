package com.example.mongolianconnected.dto;

import lombok.Data;

@Data
public class UserSignupRequest {
    private String email;
    private String password;
    private String phone;
    private String firstName;
    private String lastName;
}