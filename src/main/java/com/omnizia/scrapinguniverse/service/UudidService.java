package com.omnizia.scrapinguniverse.service;

import com.omnizia.scrapinguniverse.entity.Uudid;

import java.util.List;

import com.omnizia.scrapinguniverse.repository.UudidRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UudidService {

  private final UudidRepository uudidRepository;

  public List<Uudid> getAllUudid() {
    return uudidRepository.findAll();
  }

  public List<Uudid> getUudidByOmniziaId(String omniziaId) {
    return uudidRepository.findAllByHcpId(omniziaId);
  }

  public Uudid getUudid(String uuid, String type, String hcpId, String dftId) {
    return uudidRepository.findMatchingUudid(uuid, type, hcpId, dftId);
  }
}
