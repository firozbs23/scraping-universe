package com.omnizia.scrapinguniverse.service;

import com.omnizia.scrapinguniverse.dbcontextholder.DataSourceContextHolder;
import com.omnizia.scrapinguniverse.dto.DigitalFootprintDto;
import com.omnizia.scrapinguniverse.entity.DigitalFootprints;
import com.omnizia.scrapinguniverse.mapper.DigitalFootprintsMapper;
import com.omnizia.scrapinguniverse.repository.DigitalFootprintsRepository;
import com.omnizia.scrapinguniverse.utils.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DigitalFootprintsService {

  private final DigitalFootprintsRepository digitalFootprintsRepository;

  public List<DigitalFootprintDto> getDigitalFootprintsDto() {

    log.info("Selected database name is : {}", DataSourceContextHolder.getDataSourceType());

    List<Object[]> results = digitalFootprintsRepository.findDigitalFootprintData();

    return results.stream()
        .map(
            result ->
                DigitalFootprintDto.builder()
                    .viqIdDigitalFootprint(result[0] == null ? "" : result[0].toString())
                    .assetLabel(result[1] == null ? "" : result[1].toString())
                    .viqIdHCP(result[2] == null ? "" : result[2].toString())
                    .createdDate(result[3] == null ? "" : result[3].toString())
                    .updatedDate(result[4] == null ? "" : result[4].toString())
                    .createdByJob(result[5] == null ? "" : result[5].toString())
                    .updatedByJob(result[6] == null ? "" : result[6].toString())
                    .trackTraceStatus(result[7] == null ? "" : result[7].toString())
                    .matchingExternalId(result[8] == null ? "" : result[8].toString())
                    .firstName(result[9] == null ? "" : result[9].toString())
                    .middleName(result[10] == null ? "" : result[10].toString())
                    .lastName(result[11] == null ? "" : result[11].toString())
                    .build())
        .toList();
  }

  @Transactional
  public void updateDigitalFootprints(
      List<DigitalFootprintDto> digitalFootprintDtos, String jobId) {
    List<DigitalFootprints> digitalFootprints =
        DigitalFootprintsMapper.mapToDigitalFootprints(digitalFootprintDtos, jobId);

    digitalFootprints.forEach(
        footprint -> {
          String status = footprint.getTrackTraceStatus();
          if (StringUtils.isNotBlank(jobId) && StringUtils.isNotBlank(status)) {
            if ("Found".equals(status)
                || "Not Found".equals(status)
                || "Not Available".equals(status)
                || "Not Accessible".equals(status)) {
              String dftId = footprint.getViqIdDigitalFootprint();
              try {
                log.info("Updating digital footprint for {}", dftId);
                digitalFootprintsRepository.updateTrackTraceStatus(
                    footprint.getViqIdDigitalFootprint(),
                    footprint.getTrackTraceStatus(),
                    OffsetDateTime.now(),
                    UUID.fromString(jobId));
                log.info("Updated digital footprint for {}", dftId);
              } catch (Exception e) {
                log.error("DigitalFootprints update failed for {}", dftId, e);
              }
            }
          }
        });
  }
}
