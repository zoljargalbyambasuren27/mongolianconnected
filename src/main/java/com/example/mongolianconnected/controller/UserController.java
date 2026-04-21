package com.example.mongolianconnected.controller;

import com.example.mongolianconnected.entity.User;
import com.example.mongolianconnected.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<User> getUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/user_detail")
    public ResponseEntity<?> getUser(Authentication authentication) {
        try {
            String email = authentication.getName();
            User userDetail = service.getUserByEmail(email);

            if (userDetail == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        Map.of(
                                "status_code", 404,
                                "message", "User details not found",
                                "data", ""
                        )
                );
            }

            return ResponseEntity.ok(
                    Map.of(
                            "status_code", 200,
                            "message", "User details found successfully",
                            "data", userDetail
                    )
            );

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of(
                            "status_code", 500,
                            "message", "An error occurred while retrieving user details",
                            "error", e.getMessage()
                    )
            );
        }
    }

    @GetMapping("/test")
    public String userEndpoint() {
        return "User endpoint";
    }
}
