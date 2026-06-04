package com.jobalert.job_alert_service.service;

import com.jobalert.job_alert_service.entity.JobPosting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobFetcherService {

    private final JobPostingService jobPostingService;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://remotive.com/api")
            .codecs(configurer -> configurer
                    .defaultCodecs()
                    .maxInMemorySize(10 * 1024 * 1024)) // 10MB buffer
            .build();

    public void fetchAndSaveJobs(String keyword) {
        log.info("Fetching jobs for keyword: {}", keyword);

        webClient.get()
                .uri("/remote-jobs?search={keyword}&limit=20", keyword)
                .retrieve()
                .bodyToMono(Map.class)
                .doOnError(error -> log.error("Error fetching jobs: {}", error.getMessage()))
                .subscribe(response -> {
                    List<Map<String, Object>> jobs =
                            (List<Map<String, Object>>) response.get("jobs");

                    if (jobs == null) {
                        log.warn("No jobs found for keyword: {}", keyword);
                        return;
                    }

                    log.info("Found {} jobs for keyword: {}", jobs.size(), keyword);

                    for (Map<String, Object> job : jobs) {
                        JobPosting posting = new JobPosting();
                        posting.setTitle((String) job.get("title"));
                        posting.setCompany((String) job.get("company_name"));
                        posting.setUrl((String) job.get("url"));
                        posting.setPostedAt(LocalDateTime.now());
                        jobPostingService.save(posting);
                    }

                    log.info("Saved jobs for keyword: {}", keyword);
                });
    }
}