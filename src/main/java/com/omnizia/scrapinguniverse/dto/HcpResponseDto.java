package com.omnizia.scrapinguniverse.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.*;

@Getter
@Builder
public class HcpResponseDto {
  @JsonProperty("matching_status")
  private String matchingStatus;

  @JsonProperty("data")
  private List<HcpDto> data;
}
