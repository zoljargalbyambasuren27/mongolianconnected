package com.example.mongolianconnected.dto;

import java.util.List;

import com.example.mongolianconnected.enums.UserStatus;
import com.example.mongolianconnected.enums.VerificationLevel;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserDto {
    private Long id;                   // User entity-д тулгуурласан
    private String email;                // Username биш, email login
    private String firstName;
    private String lastName;
    private String phone;
    private String profileImageUrl;            // profileImageUrl
    private List<String> roles;          // role enum-ийг string болгож явуулна
    private UserStatus status;           // user status
    private VerificationLevel verificationLevel;  // verification status
    private Integer trustScore;          // trust score
}