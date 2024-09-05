package com.omnizia.scrapinguniverse.controller;

import com.omnizia.scrapinguniverse.dbcontextholder.DataSourceContextHolder;
import com.omnizia.scrapinguniverse.dto.HcpDto;
import com.omnizia.scrapinguniverse.service.HcpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class HcpController {
  private final HcpService hcpService;

  @GetMapping("/hcp")
  public ResponseEntity<HcpDto> getHcpByOmniziaId(
      @RequestParam("omnizia_id") String omniziaId,
      @RequestHeader(value = "omnizia-tenant", defaultValue = "") String tenant) {
    try {
      DataSourceContextHolder.setDataSourceType(tenant.trim().toLowerCase());
      var data = hcpService.getHcpByOmniziaId(omniziaId);
      return ResponseEntity.ok(data);
    } finally {
      DataSourceContextHolder.clearDataSourceType();
    }
  }
}
