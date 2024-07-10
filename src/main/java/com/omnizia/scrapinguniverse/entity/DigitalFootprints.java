package com.omnizia.scrapinguniverse.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Viquia_HCP-or-HCO_Digital_Footprints")
public class DigitalFootprints {
  @Id
  @Column(name = "\"ViqID_Digital_Footprint\"")
  @JsonProperty("ViqID_Digital_Footprint")
  private String viqIdDigitalFootprint;

  @Column(name = "\"ViqID_Asset_Type_Browser\"")
  @JsonProperty("ViqID_Asset_Type_Browser")
  private String viqIdAssetTypeBrowser;

  @Column(name = "\"Asset_Label\"")
  @JsonProperty("Asset_Label")
  private String assetLabel;

  @Column(name = "\"ViqID_HCP\"")
  @JsonProperty("ViqID_HCP")
  private String viqIdHCP;

  @Column(name = "track_trace_status")
  @JsonProperty("track_trace_status")
  private String trackTraceStatus;

  @Column(name = "created_date")
  @JsonProperty("created_date")
  private OffsetDateTime createdDate;

  @Column(name = "updated_date")
  @JsonProperty("updated_date")
  private OffsetDateTime updatedDate;

  @Column(name = "created_by_job")
  @JsonProperty("created_by_job")
  private UUID createdByJob;

  @Column(name = "updated_by_job")
  @JsonProperty("updated_by_job")
  private UUID updatedByJob;
}
