package com.omnizia.scrapinguniverse.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "hcp")
public class Hcp {
  @Id
  @JsonProperty("id")
  private int id;

  @Column(name = "expert_id")
  @JsonProperty("expert_id")
  private String expertId;

  @Column(name = "full_name_local_lang")
  @JsonProperty("full_name_local_lang")
  private String fullNameLocalLang;

  @Column(name = "first_name")
  @JsonProperty("first_name")
  private String firstName;

  @Column(name = "middle_name")
  @JsonProperty("middle_name")
  private String middleName;

  @Column(name = "last_name")
  @JsonProperty("last_name")
  private String lastName;

  @JsonProperty("affiliation")
  private String affiliation;

  @JsonProperty("country")
  private String country;

  @Column(name = "updated_at")
  @JsonProperty("updated_at")
  private String updatedAt;

  @Column(name = "specialty_code")
  @JsonProperty("specialty_code")
  private String specialtyCode;

  @Column(name = "national_id")
  @JsonProperty("national_id")
  private String nationalId;

  @Column(name = "country_iso2")
  @JsonProperty("country_iso2")
  private String countryIso2;

  @Column(name = "viquia_id")
  @JsonProperty("viquia_id")
  private String viquiaId;

  @Column(name = "company_id")
  @JsonProperty("company_id")
  private String companyId;

  @JsonProperty("gender")
  private String gender;

  @Column(name = "viq_id_academic_title")
  @JsonProperty("viq_id_academic_title")
  private String viqIdAcademicTitle;

  @Column(name = "birth_year")
  @JsonProperty("birth_year")
  private String birthYear;

  @Column(name = "graduation_year")
  @JsonProperty("graduation_year")
  private String graduationYear;

  @Column(name = "indication_city")
  @JsonProperty("indication_city")
  private String indicationCity;

  @Transient private Double ratio; // This property doesn't affect the database.
}
