package com.omnizia.scrapinguniverse.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
public class TextMatchingDto {
  @NotBlank(message = "Parameter text1 is required")
  private String text1;

  @NotBlank(message = "Parameter text2 is required")
  private String text2;

  @JsonProperty("ignore_case")
  private Boolean ignoreCase;
}
