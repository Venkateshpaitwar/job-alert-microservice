package com.jobalert.job_alert_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JobAlertServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobAlertServiceApplication.class, args);
	}

}
