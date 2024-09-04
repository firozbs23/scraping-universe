package com.omnizia.scrapinguniverse.controller;

import com.omnizia.scrapinguniverse.dbcontextholder.DataSourceContextHolder;
import com.omnizia.scrapinguniverse.dto.HcpDto;
import com.omnizia.scrapinguniverse.service.HcpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class HcpController {
  private final HcpService hcpService;

  @GetMapping("/hcp")
  public ResponseEntity<HcpDto> getHcpByOmniziaId(@RequestParam("omnizia_id") String omniziaId) {
    try {
      DataSourceContextHolder.setDataSourceType("springworks");
      var data = hcpService.getHcpByOmniziaId(omniziaId);
      return ResponseEntity.ok(data);
    } finally {
      DataSourceContextHolder.clearDataSourceType();
    }
  }
}
