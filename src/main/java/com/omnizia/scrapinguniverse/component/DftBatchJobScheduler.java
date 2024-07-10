package com.omnizia.scrapinguniverse.component;

import com.omnizia.scrapinguniverse.service.JobLauncherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DftBatchJobScheduler {

  private final JobLauncherService jobLauncherService;

  // Schedule job to run every day at 1 AM
  @Scheduled(cron = "0 0 1 * * ?")
  // @Scheduled(initialDelay = 0, fixedRate = Long.MAX_VALUE)
  public void runJob() {
    String jobId = UUID.randomUUID().toString();
    jobLauncherService.runJob(jobId);
  }
}
