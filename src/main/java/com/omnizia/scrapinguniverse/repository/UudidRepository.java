package com.omnizia.scrapinguniverse.repository;

import java.util.List;

import com.omnizia.scrapinguniverse.entity.Uudid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UudidRepository extends JpaRepository<Uudid, String> {

  List<Uudid> findAllByHcpId(String hcpId);

  @Query(
      value =
          """
          SELECT u
          FROM Uudid u
          WHERE u.uuid = :uuid
          AND u.type = :type
          AND u.hcpId = :hcpId
          AND u.dftId = :dftId
          """)
  Uudid findMatchingUudid(
      @Param("uuid") String uuid,
      @Param("type") String type,
      @Param("hcpId") String hcpId,
      @Param("dftId") String dftId);
}
