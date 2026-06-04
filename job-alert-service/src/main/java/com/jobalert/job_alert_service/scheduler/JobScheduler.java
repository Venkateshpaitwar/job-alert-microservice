package com.jobalert.job_alert_service.scheduler;

import com.jobalert.job_alert_service.entity.User;
import com.jobalert.job_alert_service.service.AlertService;
import com.jobalert.job_alert_service.service.JobFetcherService;
import com.jobalert.job_alert_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobScheduler {

    private final JobFetcherService jobFetcherService;
    private final UserService userService;
    private final AlertService alertService;

    @Scheduled(cron = "0 0 */6 * * *")
    public void fetchJobsForAllUsers() {
        log.info("Scheduler triggered — fetching jobs for all users");

        List<User> users = userService.getAllUsers();
        for (User user : users) {
            for (String keyword : user.getKeywords()) {
                jobFetcherService.fetchAndSaveJobs(keyword);
            }
        }

        // process and send alerts after fetching
        alertService.processAlerts();
    }
}