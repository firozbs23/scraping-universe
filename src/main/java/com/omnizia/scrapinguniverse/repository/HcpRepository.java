package com.omnizia.scrapinguniverse.repository;

import com.omnizia.scrapinguniverse.entity.Hcp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HcpRepository extends JpaRepository<Hcp, Long> {
  List<Hcp> findByCountryIso2IgnoreCase(String countryIso2);

  List<Hcp> findByCountryIso2IgnoreCaseAndSpecialtyCode(String countryIso2, String specialtyCode);

  Hcp findByViquiaId(String viquiaId);
}
