package com.omnizia.scrapinguniverse.service;

import com.omnizia.scrapinguniverse.dto.HcpDto;
import com.omnizia.scrapinguniverse.entity.Hcp;
import com.omnizia.scrapinguniverse.mapper.HcpMapper;
import com.omnizia.scrapinguniverse.repository.HcpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HcpService {
  private final HcpRepository hcpRepository;

  public List<Hcp> findHcpByCountryIso2(String countryIso2) {
    return hcpRepository.findByCountryIso2IgnoreCase(countryIso2);
  }

  public HcpDto getHcpByOmniziaId(String omniziaId) {
    Hcp hcp = hcpRepository.findByViquiaId(omniziaId);
    return HcpMapper.mapToHcpDto(hcp);
  }

  public String getHcpNameByOmniziaId(String omniziaId) {
    Hcp hcp = hcpRepository.findByViquiaId(omniziaId);
    String firstName = hcp.getFirstName() == null ? "" : hcp.getFirstName();
    String middleName = hcp.getMiddleName() == null ? "" : hcp.getMiddleName();
    String lastName = hcp.getLastName() == null ? "" : hcp.getLastName();
    String fullName = firstName + " " + middleName + " " + lastName;
    return removeExtraWhitespace(fullName);
  }

  private String removeExtraWhitespace(String input) {
    return input.replaceAll("\\s+", " ");
  }
}
