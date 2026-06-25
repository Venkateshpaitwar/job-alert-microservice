package com.jobalert.job_alert_service.service;

import com.jobalert.job_alert_service.entity.JobPosting;
import com.jobalert.job_alert_service.repository.JobPostingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;

    public JobPosting save(JobPosting jobPosting) {
        return jobPostingRepository.findByUrl(jobPosting.getUrl())
                .orElseGet(() -> jobPostingRepository.save(jobPosting));
    }

    @Cacheable(value = "jobs")
    public List<JobPosting> getAllJobs() {
        log.info("Fetching jobs from database...");
        return jobPostingRepository.findAll();
    }

    @CacheEvict(value = "jobs", allEntries = true)
    public void clearJobsCache() {
        log.info("Jobs cache cleared!");
    }
}