package com.omnizia.scrapinguniverse.controller;

import java.util.List;

import com.omnizia.scrapinguniverse.dbcontextholder.DataSourceContextHolder;
import com.omnizia.scrapinguniverse.dto.SpecialtyDto;
import com.omnizia.scrapinguniverse.service.SpecialtyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class SpecialtyController {
  private final SpecialtyService specialtyService;

  @GetMapping("/specialties")
  public ResponseEntity<List<SpecialtyDto>> getSpecialties() {
    try {
      DataSourceContextHolder.setDataSourceType("springworks");
      List<SpecialtyDto> data = specialtyService.getSpecialties();
      return ResponseEntity.ok(data);
    } finally {
      DataSourceContextHolder.clearDataSourceType();
    }
  }

  @GetMapping("/specialty")
  public ResponseEntity<SpecialtyDto> getSpecialtyByCode(
      @RequestParam("specialty_code") String specialtyCode) {
    try {
      DataSourceContextHolder.setDataSourceType("springworks");
      SpecialtyDto data = specialtyService.getSpecialtyByCode(specialtyCode);
      return ResponseEntity.ok(data);
    } finally {
      DataSourceContextHolder.clearDataSourceType();
    }
  }

  @GetMapping("/specialties/by-code")
  public ResponseEntity<List<SpecialtyDto>> getSpecialtyByCodes(
      @RequestParam("specialty_codes") String specialtyCodes) {
    try {
      DataSourceContextHolder.setDataSourceType("springworks");
      List<SpecialtyDto> data = specialtyService.getSpecialtiesByCodes(specialtyCodes);
      return ResponseEntity.ok(data);
    } finally {
      DataSourceContextHolder.clearDataSourceType();
    }
  }
}
