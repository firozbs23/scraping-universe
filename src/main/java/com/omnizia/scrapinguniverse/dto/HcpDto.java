package com.omnizia.scrapinguniverse.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HcpDto {
  @JsonProperty("omnizia_id")
  private String omniziaId;

  @JsonProperty("first_name")
  private String firstName;

  @JsonProperty("middle_name")
  private String middleName;

  @JsonProperty("last_name")
  private String lastName;

  @JsonProperty("matching_ratio")
  private Double ratio;

  @JsonProperty("country_iso2")
  private String countryIso2;

  @JsonProperty("indication_city")
  private String indicationCity;

  @JsonProperty("national_id")
  private String nationalId;

  @JsonProperty("specialty_code")
  private String specialtyCode;

  @JsonProperty("specialty_title")
  private String specialtyTitle;

  @JsonProperty("specialty_classification")
  private String specialtyClassification;
}
