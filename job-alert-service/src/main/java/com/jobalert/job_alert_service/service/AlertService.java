package com.jobalert.job_alert_service.service;

import com.jobalert.job_alert_service.entity.JobPosting;
import com.jobalert.job_alert_service.entity.User;
import com.jobalert.job_alert_service.repository.SentAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertService {

    private final UserService userService;
    private final JobPostingService jobPostingService;
    private final SentAlertRepository sentAlertRepository;
    private final EmailService emailService;

    @Transactional
    public void processAlerts() {
        List<User> users = userService.getAllUsers();
        List<JobPosting> allJobs = jobPostingService.getAllJobs();

        for (User user : users) {
            List<JobPosting> matchingJobs = allJobs.stream()
                    .filter(job -> !sentAlertRepository.existsByUserAndJobPosting(user, job))
                    .filter(job -> matchesKeywords(job, user.getKeywords()))
                    .collect(Collectors.toList());

            log.info("Found {} matching jobs for {}", matchingJobs.size(), user.getEmail());
            emailService.sendJobAlert(user, matchingJobs);
        }
    }

    private boolean matchesKeywords(JobPosting job, List<String> keywords) {
        String title = job.getTitle().toLowerCase();
        return keywords.stream()
                .anyMatch(keyword -> title.contains(keyword.toLowerCase()));
    }
}