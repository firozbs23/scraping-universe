package com.omnizia.scrapinguniverse.repository;

import com.omnizia.scrapinguniverse.entity.DigitalFootprints;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DigitalFootprintsRepository extends JpaRepository<DigitalFootprints, String> {

  @Query(
      value =
          """
                  SELECT
                    df."ViqID_Digital_Footprint" AS viqIdDigitalFootprint,
                    df."Asset_Label" AS assetLabel,
                    df."ViqID_HCP" AS viqIdHCP,
                    df.created_date AS createdDate,
                    df.updated_date AS updatedDate,
                    df.created_by_job AS createdByJob,
                    df.updated_by_job AS updatedByJob,
                    df.track_trace_status AS trackTraceStatus,
                    u.matching_external_id AS matchingExternalId,
                    h.first_name AS firstName,
                    h.middle_name AS middleName,
                    h.last_name AS lastName
                  FROM "Viquia_HCP-or-HCO_Digital_Footprints" df
                  LEFT JOIN t_uudid u ON df."ViqID_HCP" = u.hcp_id
                                 AND df."ViqID_Digital_Footprint" = u.dft_id
                                 AND u.type = 'ir00012'
                  LEFT JOIN hcp h ON df."ViqID_HCP" = h.viquia_id
                  """,
      nativeQuery = true)
  List<Object[]> findDigitalFootprintData();

  @Modifying
  @Transactional
  @Query(
      "UPDATE DigitalFootprints df SET df.trackTraceStatus = :trackTraceStatus, df.updatedDate = :updatedDate, df.updatedByJob = :updatedByJob WHERE df.viqIdDigitalFootprint = :viqIdDigitalFootprint")
  void updateTrackTraceStatus(
      @Param("viqIdDigitalFootprint") String viqIdDigitalFootprint,
      @Param("trackTraceStatus") String trackTraceStatus,
      @Param("updatedDate") OffsetDateTime updatedDate,
      @Param("updatedByJob") UUID updatedByJob);
}
