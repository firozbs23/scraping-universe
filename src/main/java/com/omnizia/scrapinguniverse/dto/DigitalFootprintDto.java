package com.omnizia.scrapinguniverse.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.omnizia.scrapinguniverse.utils.StringUtils;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DigitalFootprintDto {

  @JsonProperty("ViqID_Digital_Footprint")
  private String viqIdDigitalFootprint;

  @JsonProperty("Asset_Label")
  private String assetLabel;

  @JsonProperty("ViqID_HCP")
  private String viqIdHCP;

  @JsonProperty("track_trace_status")
  private String trackTraceStatus;

  @JsonProperty("created_date")
  private String createdDate;

  @JsonProperty("updated_date")
  private String updatedDate;

  @JsonProperty("created_by_job")
  private String createdByJob;

  @JsonProperty("updated_by_job")
  private String updatedByJob;

  @JsonProperty("matching_external_id")
  private String matchingExternalId;

  @JsonProperty("first_name")
  private String firstName;

  @JsonProperty("middle_name")
  private String middleName;

  @JsonProperty("last_name")
  private String lastName;

  @JsonIgnore private boolean isTrackTraceStatusChanged;

  @JsonIgnore
  private String getFullName() {
    String firstName = getFirstName() == null ? "" : getFirstName();
    String middleName = getMiddleName() == null ? "" : getMiddleName();
    String lastName = getLastName() == null ? "" : getLastName();
    String fullName = firstName + " " + middleName + " " + lastName;
    return fullName.replaceAll("\\s+", " ");
  }

  @JsonIgnore
  public String getHcpName() {
    if (StringUtils.isNotBlank(getMatchingExternalId())) {
      return getMatchingExternalId();
    }
    return getFullName();
  }

  @JsonIgnore
  public String getHcpWebsite() {
    return StringUtils.getStringOrEmpty(getAssetLabel());
  }
}
