package com.jobalert.job_alert_service.controller;

import com.jobalert.job_alert_service.entity.JobPosting;
import com.jobalert.job_alert_service.service.JobFetcherService;
import com.jobalert.job_alert_service.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobFetcherService jobFetcherService;
    private final JobPostingService jobPostingService;

    @PostMapping("/fetch/{keyword}")
    public ResponseEntity<String> fetchJobs(@PathVariable String keyword) {
        jobFetcherService.fetchAndSaveJobs(keyword);
        return ResponseEntity.ok("Fetching jobs for: " + keyword);
    }

    @GetMapping
    public ResponseEntity<List<JobPosting>> getAllJobs() {
        return ResponseEntity.ok(jobPostingService.getAllJobs());
    }
}