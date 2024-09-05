package com.omnizia.scrapinguniverse.controller;

import com.omnizia.scrapinguniverse.dbcontextholder.DataSourceContextHolder;
import com.omnizia.scrapinguniverse.dto.DigitalFootprintDto;
import com.omnizia.scrapinguniverse.service.DigitalFootprintsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class DigitalFootprintsController {

  private final DigitalFootprintsService digitalFootprintsService;

  @GetMapping("/digital-footprints")
  public ResponseEntity<List<DigitalFootprintDto>> getDigitalFootprints(
      @RequestHeader(value = "omnizia-tenant", defaultValue = "") String tenant) {
    try {
      DataSourceContextHolder.setDataSourceType(tenant.trim().toLowerCase());
      log.info("Getting digital footprints. Endpoint: /api/v2/digital-footprints");
      List<DigitalFootprintDto> digitalFootprintDtos =
          digitalFootprintsService.getDigitalFootprintsDto();

      log.info(
          "{}: Digital footprints size : {}",
          this.getClass().getSimpleName(),
          digitalFootprintDtos.size());
      return ResponseEntity.ok(digitalFootprintDtos);
    } finally {
      DataSourceContextHolder.clearDataSourceType();
    }
  }

  @PutMapping("/digital-footprints")
  public ResponseEntity<String> updateDigitalFootprints(
      @RequestBody List<DigitalFootprintDto> digitalFootprintDtos,
      @RequestParam(name = "job_id", required = false) String jobId,
      @RequestHeader(value = "omnizia-tenant", defaultValue = "") String tenant) {
    try {
      DataSourceContextHolder.setDataSourceType(tenant.trim().toLowerCase());
      log.info("Updating digital footprints. Endpoint: /api/v2/digital-footprints");
      digitalFootprintsService.updateDigitalFootprints(digitalFootprintDtos, jobId);
      return ResponseEntity.ok("Data updated");
    } finally {
      DataSourceContextHolder.clearDataSourceType();
    }
  }
}
