package com.omnizia.scrapinguniverse.mapper;


import com.omnizia.scrapinguniverse.dto.HcpDto;
import com.omnizia.scrapinguniverse.entity.Hcp;

import java.util.List;

public class HcpMapper {
  public static List<HcpDto> mapToHcpFoundListDto(List<Hcp> hcpList) {
    return hcpList.stream()
        .map(
            hcp ->
                HcpDto.builder()
                    .omniziaId(hcp.getViquiaId())
                    .firstName(hcp.getFirstName())
                    .middleName(hcp.getMiddleName())
                    .lastName(hcp.getLastName())
                    .ratio(hcp.getRatio())
                    .countryIso2(hcp.getCountryIso2())
                    .indicationCity(hcp.getIndicationCity())
                    .nationalId(hcp.getNationalId())
                    .specialtyCode(hcp.getSpecialtyCode())
                    .specialtyTitle("")
                    .specialtyClassification("")
                    .build())
        .toList();
  }

  public static HcpDto mapToHcpDto(Hcp hcp) {
    return HcpDto.builder()
        .omniziaId(hcp.getViquiaId())
        .countryIso2(hcp.getCountryIso2())
        .firstName(hcp.getFirstName())
        .middleName(hcp.getMiddleName())
        .lastName(hcp.getLastName())
        .indicationCity(hcp.getIndicationCity())
        .nationalId(hcp.getNationalId())
        .specialtyCode(hcp.getSpecialtyCode())
        .build();
  }
}
