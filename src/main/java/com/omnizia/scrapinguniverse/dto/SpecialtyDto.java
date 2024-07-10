package com.omnizia.scrapinguniverse.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpecialtyDto {
  @JsonProperty("specialty_code")
  private String specialtyCode;

  private String classification;
  private String title;
  private String description;

  @JsonProperty("created_at")
  private String createdAt;

  @JsonProperty("updated_at")
  private String updatedAt;
}
