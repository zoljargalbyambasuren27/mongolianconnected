package com.example.mongolianconnected.controller;

import com.example.mongolianconnected.entity.User;
import com.example.mongolianconnected.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/test")
    public String userEndpoint() {
        return "User endpoint";
    }
}
