package com.jobalert.job_alert_service.controller;

import com.jobalert.job_alert_service.entity.User;
import com.jobalert.job_alert_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/subscribe")
    public ResponseEntity<User> subscribe(@RequestBody User user) {
        return ResponseEntity.ok(
                userService.subscribe(user.getEmail(), user.getKeywords(), user.getFrequency())
        );
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/unsubscribe/{email}")
    public ResponseEntity<String> unsubscribe(@PathVariable String email) {
        userService.unsubscribe(email);
        return ResponseEntity.ok("Unsubscribed successfully");
    }
}