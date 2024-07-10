package com.omnizia.scrapinguniverse.mapper;

import com.omnizia.scrapinguniverse.dto.SpecialtyDto;
import com.omnizia.scrapinguniverse.entity.Specialty;

import java.util.List;

public class SpecialtyMapper {
  public static List<SpecialtyDto> mapToSpecialtyDto(List<Specialty> specialtyList) {
    return specialtyList.stream()
        .map(
            specialty ->
                SpecialtyDto.builder()
                    .specialtyCode(specialty.getSpecialtyCode())
                    .classification(specialty.getClassification())
                    .title(specialty.getTitle())
                    .description(specialty.getDescription())
                    .createdAt(specialty.getCreatedAt())
                    .updatedAt(specialty.getUpdatedAt())
                    .build())
        .toList();
  }

  public static SpecialtyDto mapToSpecialtyDto(Specialty specialty) {
    return SpecialtyDto.builder()
        .specialtyCode(specialty.getSpecialtyCode())
        .classification(specialty.getClassification())
        .title(specialty.getTitle())
        .description(specialty.getDescription())
        .createdAt(specialty.getCreatedAt())
        .updatedAt(specialty.getUpdatedAt())
        .build();
  }
}
