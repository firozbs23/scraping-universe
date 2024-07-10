package com.omnizia.scrapinguniverse.service;

import com.omnizia.scrapinguniverse.dbcontextholder.DataSourceContextHolder;
import com.omnizia.scrapinguniverse.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobLauncherService {

  private final JobLauncher jobLauncher;
  private final Job job;

  public void runJob(String jobId) {
    Thread.ofVirtual()
        .start(
            () -> {
              String currentTime = TimeUtils.getCurrentTimeUTC();
              try {
                DataSourceContextHolder.setDataSourceType("dataSource");
                log.info("Running job {} in {}", job.getName(), currentTime);
                JobParameters jobParameters =
                    new JobParametersBuilder()
                        .addString("jobId", jobId)
                        .addDate("startTime", new Date())
                        .toJobParameters();
                jobLauncher.run(job, jobParameters);
              } catch (JobExecutionException e) {
                currentTime = TimeUtils.getCurrentTimeUTC();
                log.error("Failed job {} in {}", job.getName(), currentTime, e);
              } finally {
                currentTime = TimeUtils.getCurrentTimeUTC();
                log.info("Finished job {} in {}", job.getName(), currentTime);
                DataSourceContextHolder.clearDataSourceType();
              }
            });
  }
}
