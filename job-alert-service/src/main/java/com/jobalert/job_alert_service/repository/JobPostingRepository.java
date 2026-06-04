package com.jobalert.job_alert_service.repository;

import com.jobalert.job_alert_service.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    Optional<JobPosting> findByUrl(String url);
}