package com.jobalert.job_alert_service.service;

import com.jobalert.job_alert_service.entity.JobPosting;
import com.jobalert.job_alert_service.entity.SentAlert;
import com.jobalert.job_alert_service.entity.User;
import com.jobalert.job_alert_service.repository.SentAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final SentAlertRepository sentAlertRepository;

    public void sendJobAlert(User user, List<JobPosting> matchingJobs) {
        if (matchingJobs.isEmpty()) return;

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(user.getEmail());
            helper.setSubject("🚀 New Job Alerts For You!");
            helper.setText(buildEmailBody(user, matchingJobs), true);

            mailSender.send(message);

            // save to sent_alerts to prevent duplicates
            for (JobPosting job : matchingJobs) {
                SentAlert sentAlert = new SentAlert();
                sentAlert.setUser(user);
                sentAlert.setJobPosting(job);
                sentAlert.setSentAt(LocalDateTime.now());
                sentAlertRepository.save(sentAlert);
            }

            log.info("Email sent to {} with {} jobs", user.getEmail(), matchingJobs.size());

        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", user.getEmail(), e.getMessage());
        }
    }

    private String buildEmailBody(User user, List<JobPosting> jobs) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h2>Hi! Here are your latest job matches 👋</h2>");
        sb.append("<p>Based on your keywords: <b>")
                .append(String.join(", ", user.getKeywords()))
                .append("</b></p><hr/>");

        for (JobPosting job : jobs) {
            sb.append("<div style='margin-bottom:20px;'>")
                    .append("<h3>").append(job.getTitle()).append("</h3>")
                    .append("<p>🏢 ").append(job.getCompany()).append("</p>")
                    .append("<a href='").append(job.getUrl()).append("'>View Job →</a>")
                    .append("</div><hr/>");
        }

        return sb.toString();
    }
}