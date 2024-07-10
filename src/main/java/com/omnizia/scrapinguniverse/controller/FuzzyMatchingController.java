package com.omnizia.scrapinguniverse.controller;

import com.omnizia.scrapinguniverse.dbcontextholder.DataSourceContextHolder;
import com.omnizia.scrapinguniverse.dto.HcpRequestDto;
import com.omnizia.scrapinguniverse.dto.HcpResponseDto;
import com.omnizia.scrapinguniverse.dto.TextMatchingDto;
import com.omnizia.scrapinguniverse.service.FuzzyMatchingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class FuzzyMatchingController {
  private final FuzzyMatchingService fuzzyMatchingService;

  @PostMapping("/hcp/search/fuzzy")
  public ResponseEntity<HcpResponseDto> searchHcpByFuzzyMatch(
      @Valid @RequestBody HcpRequestDto hcpDto) {
    try {
      DataSourceContextHolder.setDataSourceType("mcd");
      log.debug("Hit endpoint : /hcp/search/fuzzy");
      HcpResponseDto data = fuzzyMatchingService.findHcpByFuzzyMatching2(hcpDto);
      return ResponseEntity.ok(data);
    } finally {
      DataSourceContextHolder.clearDataSourceType();
    }
  }

  @PostMapping("/fuzzy-match")
  public ResponseEntity<String> fuzzyMatchBetweenTexts(
      @Valid @RequestBody TextMatchingDto textMatchingDto) {
    try {
      DataSourceContextHolder.setDataSourceType("mcd");
      String data = fuzzyMatchingService.fuzzyMatchRatio(textMatchingDto);
      return ResponseEntity.ok(data);
    } finally {
      DataSourceContextHolder.clearDataSourceType();
    }
  }
}
