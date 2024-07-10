package com.omnizia.scrapinguniverse.mapper;

import com.omnizia.scrapinguniverse.dto.DigitalFootprintDto;
import com.omnizia.scrapinguniverse.entity.DigitalFootprints;
import com.omnizia.scrapinguniverse.utils.TimeUtils;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class DigitalFootprintsMapper {

  public static List<DigitalFootprints> mapToDigitalFootprints(
      List<DigitalFootprintDto> digitalFootprintDtos, String uuid) {
    return digitalFootprintDtos.stream()
        .map(
            footprint ->
                DigitalFootprints.builder()
                    .viqIdDigitalFootprint(footprint.getViqIdDigitalFootprint())
                    .trackTraceStatus(footprint.getTrackTraceStatus())
                    .updatedDate(OffsetDateTime.now())
                    .updatedByJob(UUID.fromString(uuid))
                    .build())
        .toList();
  }
}
