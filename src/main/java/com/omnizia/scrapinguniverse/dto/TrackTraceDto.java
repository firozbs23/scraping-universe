package com.omnizia.scrapinguniverse.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrackTraceDto {
  @JsonProperty("hcp_name")
  private String hcpName;

  @JsonProperty("hcp_website")
  private String hcpWebsite;

  @Setter private String status;

  @Setter private String timestamp;

  @JsonProperty("dft_id")
  private String dftId;
}
