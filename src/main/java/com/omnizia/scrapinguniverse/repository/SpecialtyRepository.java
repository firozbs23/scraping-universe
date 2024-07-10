package com.omnizia.scrapinguniverse.repository;

import java.util.List;

import com.omnizia.scrapinguniverse.entity.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, String> {
  Specialty findBySpecialtyCode(String specialtyCode);

  List<Specialty> findBySpecialtyCodeIn(List<String> specialtyCodes);
}
