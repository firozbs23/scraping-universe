package com.omnizia.scrapinguniverse.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
public class HcpRequestDto {
  @JsonProperty("country_iso2")
  private String countryISO2;

  @JsonProperty("first_name")
  private String firstName;

  @JsonProperty("last_name")
  private String lastName;

  @JsonProperty("specialty_code")
  private String specialtyCode;

  @JsonProperty("indication_city")
  private String indicationCity;
}
