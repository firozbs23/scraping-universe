package com.omnizia.scrapinguniverse.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Data
@Entity
@Table(name = "m_specialties")
public class Specialty {
  @Id
  @Column(name = "specialty_code")
  @JsonProperty("specialty_code")
  private String specialtyCode;

  private String classification;
  private String title;
  private String description;

  @Column(name = "created_at")
  @JsonProperty("created_at")
  private String createdAt;

  @Column(name = "updated_at")
  @JsonProperty("updated_at")
  private String updatedAt;
}
