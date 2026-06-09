package com.jobalert.job_alert_service.controller;

import com.jobalert.job_alert_service.entity.JobPosting;
import com.jobalert.job_alert_service.service.JobFetcherService;
import com.jobalert.job_alert_service.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jobalert.job_alert_service.service.AlertService;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobFetcherService jobFetcherService;
    private final JobPostingService jobPostingService;
    private final AlertService alertService;

    @PostMapping("/fetch/{keyword}")
    public ResponseEntity<String> fetchJobs(@PathVariable String keyword) {
        jobFetcherService.fetchAndSaveJobs(keyword);
        return ResponseEntity.ok("Fetching jobs for: " + keyword);
    }

    @GetMapping
    public ResponseEntity<List<JobPosting>> getAllJobs() {
        return ResponseEntity.ok(jobPostingService.getAllJobs());
    }

    @PostMapping("/trigger-alerts")
    public ResponseEntity<String> triggerAlerts() {
        alertService.processAlerts();
        return ResponseEntity.ok("Alerts processed!");
    }
    @GetMapping("/match-test/{email}")
    public ResponseEntity<List<JobPosting>> testMatch(@PathVariable String email) {
        List<JobPosting> allJobs = jobPostingService.getAllJobs();

        return ResponseEntity.ok(
                allJobs.stream()
                        .filter(job -> job.getTitle().toLowerCase().contains("java"))
                        .collect(java.util.stream.Collectors.toList())
        );
    }
}