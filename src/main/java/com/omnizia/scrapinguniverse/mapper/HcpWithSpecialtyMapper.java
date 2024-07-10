package com.omnizia.scrapinguniverse.mapper;

import com.omnizia.scrapinguniverse.dto.HcpDto;
import com.omnizia.scrapinguniverse.dto.SpecialtyDto;
import com.omnizia.scrapinguniverse.entity.Hcp;

import java.util.List;

public class HcpWithSpecialtyMapper {
  public static List<HcpDto> mapHcpAndSpecialtyDtoToHcpDto(
      List<Hcp> hcpList, List<SpecialtyDto> specialties) {
    return hcpList.stream()
        .filter(hcp -> hcp.getViquiaId() != null && !hcp.getViquiaId().isBlank())
        .map(
            hcp -> {
              // Get matched specialty
              SpecialtyDto matchingSpecialty =
                  specialties.stream()
                      .filter(
                          specialty -> specialty.getSpecialtyCode().equals(hcp.getSpecialtyCode()))
                      .findFirst()
                      .orElse(SpecialtyDto.builder().build());

              return HcpDto.builder()
                  .omniziaId(hcp.getViquiaId())
                  .firstName(hcp.getFirstName())
                  .middleName(hcp.getMiddleName())
                  .lastName(hcp.getLastName())
                  .ratio(hcp.getRatio())
                  .countryIso2(hcp.getCountryIso2())
                  .indicationCity(hcp.getIndicationCity())
                  .nationalId(hcp.getNationalId())
                  .specialtyCode(hcp.getSpecialtyCode())
                  .specialtyTitle(matchingSpecialty.getTitle())
                  .specialtyClassification(matchingSpecialty.getClassification())
                  .build();
            })
        .toList();
  }

  public static List<HcpDto> mapHcpDtoAndSpecialtyDtoToHcpDto(
      List<HcpDto> hcpList, List<SpecialtyDto> specialties) {
    return hcpList.stream()
        .filter(hcp -> hcp.getOmniziaId() != null && !hcp.getOmniziaId().isBlank())
        .map(
            hcp -> {
              // Get matched specialty
              SpecialtyDto matchingSpecialty =
                  specialties.stream()
                      .filter(
                          specialty -> specialty.getSpecialtyCode().equals(hcp.getSpecialtyCode()))
                      .findFirst()
                      .orElse(SpecialtyDto.builder().build());

              return HcpDto.builder()
                  .omniziaId(hcp.getOmniziaId())
                  .firstName(hcp.getFirstName())
                  .middleName(hcp.getMiddleName())
                  .lastName(hcp.getLastName())
                  .ratio(hcp.getRatio())
                  .countryIso2(hcp.getCountryIso2())
                  .indicationCity(hcp.getIndicationCity())
                  .nationalId(hcp.getNationalId())
                  .specialtyCode(hcp.getSpecialtyTitle())
                  .specialtyTitle(matchingSpecialty.getTitle())
                  .specialtyClassification(matchingSpecialty.getClassification())
                  .build();
            })
        .toList();
  }
}
