package com.jobalert.job_alert_service.service;

import com.jobalert.job_alert_service.entity.User;
import com.jobalert.job_alert_service.exception.ResourceNotFoundException;
import com.jobalert.job_alert_service.repository.UserRepository;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User subscribe(String email, List<String> keywords, String frequency) {
        return userRepository.findByEmail(email)
                .map(existingUser -> {
                    // add new keywords without duplicates
                    List<String> updatedKeywords = new java.util.ArrayList<>(existingUser.getKeywords());
                    for (String keyword : keywords) {
                        if (!updatedKeywords.contains(keyword)) {
                            updatedKeywords.add(keyword);
                        }
                    }
                    existingUser.setKeywords(updatedKeywords);
                    existingUser.setFrequency(frequency);
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setKeywords(keywords);
                    newUser.setFrequency(frequency);
                    return userRepository.save(newUser);
                });
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void unsubscribe(String email) {
        User user = userRepository.findByEmail(email)
                        .orElseThrow(() ->new ResourceNotFoundException("User not foind with email :" + email));
        userRepository.delete(user);
    }
}