package com.omnizia.scrapinguniverse.service;

import java.util.Arrays;
import java.util.List;

import com.omnizia.scrapinguniverse.dto.SpecialtyDto;
import com.omnizia.scrapinguniverse.entity.Specialty;
import com.omnizia.scrapinguniverse.mapper.SpecialtyMapper;
import com.omnizia.scrapinguniverse.repository.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecialtyService {
  private final SpecialtyRepository specialtyRepository;

  public List<SpecialtyDto> getSpecialties() {
    List<Specialty> specialties = specialtyRepository.findAll();
    return SpecialtyMapper.mapToSpecialtyDto(specialties);
  }

  public SpecialtyDto getSpecialtyByCode(String specialtyCode) {
    Specialty specialty = specialtyRepository.findBySpecialtyCode(specialtyCode);
    return SpecialtyMapper.mapToSpecialtyDto(specialty);
  }

  public List<SpecialtyDto> getSpecialtiesByCodes(String specialtyCodesStr) {
    List<String> specialtyCodes = Arrays.stream(specialtyCodesStr.split(",")).toList();
    List<Specialty> specialties = specialtyRepository.findBySpecialtyCodeIn(specialtyCodes);
    return SpecialtyMapper.mapToSpecialtyDto(specialties);
  }

  public List<SpecialtyDto> getSpecialtiesByCodes(List<String> specialtyCodes) {
    List<Specialty> specialties = specialtyRepository.findBySpecialtyCodeIn(specialtyCodes);
    return SpecialtyMapper.mapToSpecialtyDto(specialties);
  }
}
