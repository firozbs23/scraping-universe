package com.omnizia.scrapinguniverse.controller;

import com.omnizia.scrapinguniverse.service.JobLauncherService;
import com.omnizia.scrapinguniverse.service.TrackTraceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
public class TrackAndTraceController {

  private final TrackTraceService trackAndTraceService;
  private final JobLauncherService jobLauncherService;

  @PostMapping("/hcp/check/status")
  public ResponseEntity<?> statusCheck(@RequestBody Object requestBody) {
    log.info("Hit endpoint /hcp/check/status");
    var data = trackAndTraceService.getStatus(requestBody);
    return ResponseEntity.ok(data);
  }

  @GetMapping("/hcp/start/job")
  public ResponseEntity<?> startJob() {
    log.info("Hit endpoint /hcp/start/job");
    String jobId = UUID.randomUUID().toString();
    jobLauncherService.runJob(jobId);
    return ResponseEntity.ok("Job Started with job_id: " + jobId);
  }
}
