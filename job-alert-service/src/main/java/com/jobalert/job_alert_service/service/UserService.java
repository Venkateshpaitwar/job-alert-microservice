package com.jobalert.job_alert_service.service;

import com.jobalert.job_alert_service.entity.User;
import com.jobalert.job_alert_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User subscribe(String email, List<String> keywords, String frequency) {
        User user = new User();
        user.setEmail(email);
        user.setKeywords(keywords);
        user.setFrequency(frequency);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void unsubscribe(String email) {
        userRepository.findByEmail(email)
                .ifPresent(userRepository::delete);
    }
}