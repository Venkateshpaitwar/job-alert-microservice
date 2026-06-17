package com.jobalert.job_alert_service.controller;

import com.jobalert.job_alert_service.entity.JobPosting;
import com.jobalert.job_alert_service.service.AlertMessageProducer;
import com.jobalert.job_alert_service.service.AlertService;
import com.jobalert.job_alert_service.service.JobFetcherService;
import com.jobalert.job_alert_service.service.JobPostingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@Tag(name = "Job Management", description = "APIs for fetching and managing job listings")
public class JobController {

    private final JobFetcherService jobFetcherService;
    private final JobPostingService jobPostingService;
    private final AlertMessageProducer alertMessageProducer ;

    @Operation(summary = "Fetch jobs by keyword", description = "Manually fetch jobs from Remotive API for a given keyword")
    @PostMapping("/fetch/{keyword}")
    public ResponseEntity<String> fetchJobs(@PathVariable String keyword) {
        jobFetcherService.fetchAndSaveJobs(keyword);
        return ResponseEntity.ok("Fetching jobs for: " + keyword);
    }

    @Operation(summary = "Get all saved jobs", description = "Returns all job postings stored in the database")
    @GetMapping
    public ResponseEntity<List<JobPosting>> getAllJobs() {
        return ResponseEntity.ok(jobPostingService.getAllJobs());
    }

    @Operation(summary = "Trigger email alerts", description = "Manually trigger job matching and send email alerts to all subscribers")
    @PostMapping("/trigger-alerts")
    public ResponseEntity<String> triggerAlerts() {
        alertMessageProducer.publishAlertTrigger("Manual trigger from API");
        return ResponseEntity.ok("Alert message published to queue!");
    }
}