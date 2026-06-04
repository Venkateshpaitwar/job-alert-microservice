package com.jobalert.job_alert_service.service;

import com.jobalert.job_alert_service.entity.JobPosting;
import com.jobalert.job_alert_service.repository.JobPostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;

    public JobPosting save(JobPosting jobPosting) {
        // deduplication — don't save if URL already exists
        return jobPostingRepository.findByUrl(jobPosting.getUrl())
                .orElseGet(() -> jobPostingRepository.save(jobPosting));
    }

    public List<JobPosting> getAllJobs() {
        return jobPostingRepository.findAll();
    }
}