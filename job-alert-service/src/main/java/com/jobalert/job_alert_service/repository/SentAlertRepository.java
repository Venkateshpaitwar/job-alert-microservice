package com.jobalert.job_alert_service.repository;

import com.jobalert.job_alert_service.entity.SentAlert;
import com.jobalert.job_alert_service.entity.User;
import com.jobalert.job_alert_service.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SentAlertRepository extends JpaRepository<SentAlert, Long> {
    boolean existsByUserAndJobPosting(User user, JobPosting jobPosting);
}