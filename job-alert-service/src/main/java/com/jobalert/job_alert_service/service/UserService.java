package com.jobalert.job_alert_service.service;

import com.jobalert.job_alert_service.entity.User;
import com.jobalert.job_alert_service.exception.ResourceNotFoundException;
import com.jobalert.job_alert_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(String username, String rawPassword, String email,
                         List<String> keywords, String frequency) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setEmail(email);
        user.setKeywords(keywords);
        user.setFrequency(frequency);

        return userRepository.save(user);
    }

    public User subscribe(String email, List<String> keywords, String frequency) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No account found for email: " + email + ". Please register first."));

        List<String> updatedKeywords = new java.util.ArrayList<>(existingUser.getKeywords());
        for (String keyword : keywords) {
            if (!updatedKeywords.contains(keyword)) {
                updatedKeywords.add(keyword);
            }
        }
        existingUser.setKeywords(updatedKeywords);
        existingUser.setFrequency(frequency);
        return userRepository.save(existingUser);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void unsubscribe(String email) {
        User user = userRepository.findByEmail(email)
                        .orElseThrow(() ->new ResourceNotFoundException("User not found with email :" + email));
        userRepository.delete(user);
    }
}