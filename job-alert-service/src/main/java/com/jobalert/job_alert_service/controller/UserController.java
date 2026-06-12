package com.jobalert.job_alert_service.controller;

import com.jobalert.job_alert_service.entity.User;
import com.jobalert.job_alert_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing job alert subscriptions")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Subscribe to job alerts", description = "Register email with keywords to receive job alerts")
    @PostMapping("/subscribe")
    public ResponseEntity<User> subscribe(@RequestBody User user) {
        return ResponseEntity.ok(
                userService.subscribe(user.getEmail(), user.getKeywords(), user.getFrequency())
        );
    }

    @Operation(summary = "Get all subscribers", description = "Returns list of all subscribed users")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Unsubscribe from alerts", description = "Remove user subscription by email")
    @DeleteMapping("/unsubscribe/{email}")
    public ResponseEntity<String> unsubscribe(@PathVariable String email) {
        userService.unsubscribe(email);
        return ResponseEntity.ok("Unsubscribed successfully");
    }
}