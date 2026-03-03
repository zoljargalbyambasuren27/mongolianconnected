package com.example.mongolianconnected.controller;

import com.example.mongolianconnected.dto.*;
import com.example.mongolianconnected.entity.RefreshToken;
import com.example.mongolianconnected.entity.User;
import com.example.mongolianconnected.enums.UserRole;
import com.example.mongolianconnected.enums.UserStatus;
import com.example.mongolianconnected.enums.VerificationLevel;
import com.example.mongolianconnected.exception.InvalidCredentialsException;
import com.example.mongolianconnected.exception.UserNotFoundException;
import com.example.mongolianconnected.repository.UserRepository;
import com.example.mongolianconnected.security.JwtUtil;
import com.example.mongolianconnected.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Injected via @Bean
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    // ------------------- SIGNUP -------------------
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserDto>> register(@RequestBody UserSignupRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.ok(ApiResponse.<UserDto>builder()
                    .success(false)
                    .message("Email already in use")
                    .data(null)
                    .errorCode("EMAIL_EXISTS")
                    .build());
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            return ResponseEntity.ok(ApiResponse.<UserDto>builder()
                    .success(false)
                    .message("Phone number already in use")
                    .data(null)
                    .errorCode("PHONE_EXISTS")
                    .build());
        }

        User user = User.builder()
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(UserRole.USER)
                .status(UserStatus.PENDING_VERIFICATION)
                .verificationLevel(VerificationLevel.PHONE_VERIFIED)
                .trustScore(0)
                .build();

        User saved = userRepository.save(user);

        UserDto userDto = UserDto.builder()
                .id(saved.getId())
                .email(saved.getEmail())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .phone(saved.getPhone())
                .roles(List.of(saved.getRole().name()))
                .status(saved.getStatus())
                .verificationLevel(saved.getVerificationLevel())
                .trustScore(saved.getTrustScore())
                .build();

        return ResponseEntity.ok(ApiResponse.<UserDto>builder()
                .success(true)
                .message("User registered successfully!")
                .data(userDto)
                .errorCode(null)
                .build());
    }

    // ------------------- LOGIN -------------------
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLoginResponse>> login(@RequestBody UserLoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String jwt = jwtUtil.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .profileImageUrl(user.getProfileImageUrl())
                .roles(List.of(user.getRole().name()))
                .status(user.getStatus())
                .verificationLevel(user.getVerificationLevel())
                .trustScore(user.getTrustScore())
                .build();

        UserLoginResponse response = UserLoginResponse.builder()
                .user(userDto)
                .permissions(userDto.getRoles())
                .accessToken(jwt)
                .refreshToken(refreshToken.getToken())
                .build();

        ApiResponse<UserLoginResponse> apiResponse = ApiResponse.<UserLoginResponse>builder()
                .success(true)
                .message("Login successful")
                .data(response)
                .errorCode(null)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // ------------------- REFRESH TOKEN -------------------
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<Map<String, String>>> refresh(@RequestBody Map<String, String> request) {

        String refreshTokenStr = request.get("refreshToken");

        if (refreshTokenStr == null || !refreshTokenService.isTokenValid(refreshTokenStr)) {
            return ResponseEntity.ok(ApiResponse.<Map<String, String>>builder()
                    .success(false)
                    .message("Invalid or missing refresh token")
                    .data(null)
                    .errorCode("INVALID_REFRESH_TOKEN")
                    .build());
        }

        User user = refreshTokenService.getUserByToken(refreshTokenStr);

        String newJwt = jwtUtil.generateToken(user);

        return ResponseEntity.ok(ApiResponse.<Map<String, String>>builder()
                .success(true)
                .message("Token refreshed successfully")
                .data(Map.of("jwt", newJwt))
                .errorCode(null)
                .build());
    }

    // ------------------- LOGOUT -------------------
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logoutUser(@RequestBody UserLogoutRequest request) {
        refreshTokenService.revokeToken(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Logged out successfully")
                .data(null)
                .errorCode(null)
                .build());
    }
}