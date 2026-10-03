package com.jobalert.job_alert_service.controller;

import com.jobalert.job_alert_service.entity.User;
import com.jobalert.job_alert_service.repository.UserRepository;
import com.jobalert.job_alert_service.security.JwtUtil;
import com.jobalert.job_alert_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for user registration and login")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Operation(summary = "Register a new user", description = "Create account with username, password, email, keywords")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            userService.register(
                    request.getUsername(),
                    request.getPassword(),
                    request.getEmail(),
                    request.getKeywords(),
                    request.getFrequency()
            );
            return ResponseEntity.ok("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Login", description = "Authenticate user and return JWT token")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getUsername());

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    // DTOs
    @lombok.Data
    static class RegisterRequest {
        private String username;
        private String password;
        private String email;
        private java.util.List<String> keywords;
        private String frequency;
    }

    @lombok.Data
    static class LoginRequest {
        private String username;
        private String password;
    }
}