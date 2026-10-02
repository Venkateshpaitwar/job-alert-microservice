package com.jobalert.job_alert_service.dto;

import java.util.List;
public record UserResponse(
        Long id,
        String email,
        String username,
        List<String> keywords,
        String frequency
) {
    public static UserResponse from(com.jobalert.job_alert_service.entity.User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getKeywords(),
                user.getFrequency()
        );
    }
}