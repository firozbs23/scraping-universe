package com.omnizia.scrapinguniverse.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.omnizia.scrapinguniverse.dto.*;
import com.omnizia.scrapinguniverse.entity.Hcp;
import com.omnizia.scrapinguniverse.exception.CustomException;
import com.omnizia.scrapinguniverse.mapper.HcpWithSpecialtyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.omnizia.scrapinguniverse.utils.Constants.FUZZY_RATIO;
import static com.omnizia.scrapinguniverse.utils.Constants.IGNORE_CASE;
import static com.omnizia.scrapinguniverse.utils.StaticText.*;

@Service
@RequiredArgsConstructor
public class FuzzyMatchingService {
  private final HcpService hcpService;
  private final SpecialtyService specialtyService;
  private final LevenshteinAlgorithmService levenshteinAlgorithmService;

  public double fuzzyMatchRatio(String text1, String text2) {
    text1 = text1 == null ? "" : text1;
    text2 = text2 == null ? "" : text2;
    int distance = levenshteinAlgorithmService.levenshteinDistance(text1, text2);
    int maxLength = Math.max(text1.length(), text2.length());
    // Calculate the fuzzy match ratio
    double ratio = 1.0 - ((double) distance / maxLength);
    return ratio * 100; // Convert ratio to percentage
  }

  public double fuzzyMatchRatio(String string1, String string2, boolean isIgnoreCase) {
    string1 = string1 == null ? "" : string1;
    string2 = string2 == null ? "" : string2;
    if (isIgnoreCase) return fuzzyMatchRatio(string1.toLowerCase(), string2.toLowerCase());
    return fuzzyMatchRatio(string1, string2);
  }

  public String fuzzyMatchRatio(TextMatchingDto textMatchingDto) {
    boolean ignoreCase = textMatchingDto.getIgnoreCase() != null && textMatchingDto.getIgnoreCase();
    double fuzzyRatio =
        fuzzyMatchRatio(textMatchingDto.getText1(), textMatchingDto.getText2(), ignoreCase);

    return "Fuzzy match ratio of "
        + textMatchingDto.getText1()
        + " and "
        + textMatchingDto.getText2()
        + " is : "
        + fuzzyRatio;
  }

  public HcpResponseDto findHcpByFuzzyMatching2(HcpRequestDto hcpDto) {
    validateNameAndCountry(hcpDto);
    var hcpList = hcpService.findHcpByCountryIso2(hcpDto.getCountryISO2());
    if (hcpList.isEmpty())
      return HcpResponseDto.builder().matchingStatus(TYPE_NOT_FOUND).data(List.of()).build();

    // lastName matching from hcpList
    var hcpListByLastName = getGoodMatchedHcpListByLastName(hcpList, hcpDto);

    if (hcpListByLastName.isEmpty()) {
      // No lastName match found. Try to match firstName.
      var hcpListByFirstName = getGoodMatchedHcpListByFirstName(hcpList, hcpDto);

      if (hcpListByFirstName.isEmpty()) {
        // No lastName and no firstName match found. Return Uncertain matching list from hcpList
        return HcpResponseDto.builder()
            .matchingStatus(STATUS_UNCERTAIN_MATCHES)
            .data(getTopListOfHcpFoundDto(hcpList, hcpDto))
            .build();
      }

      // No lastName, but firstName match found. So matching status will be name change.
      return HcpResponseDto.builder()
          .matchingStatus(STATUS_NAMES_CHANGED)
          .data(getTopListOfHcpFoundDto(hcpListByFirstName, hcpDto))
          .build();
    }

    // lastName match found. Start matching firstName
    var hcpListByLastNameAndFirstName = getGoodMatchedHcpListByFirstName(hcpListByLastName, hcpDto);
    if (hcpListByLastNameAndFirstName.isEmpty()) {
      // lastName match found, but no firstName. Return Uncertain matching list
      return HcpResponseDto.builder()
          .matchingStatus(STATUS_UNCERTAIN_MATCHES)
          .data(getTopListOfHcpFoundDto(hcpListByLastName, hcpDto))
          .build();
    }

    // lastName and firstName matching found.
    // Try to match specialtyCode.
    var hcpFoundList = getTopListOfHcpFoundDto(hcpListByLastNameAndFirstName, hcpDto);
    var hasSpecialtyMatch = checkIfSpecialtyCodeMatches(hcpFoundList, hcpDto);
    var matchingStatus =
        (!StringUtils.hasText(hcpDto.getSpecialtyCode()) || hasSpecialtyMatch)
            ? STATUS_GOOD_MATCHES
            : STATUS_WRONG_SPECIALTY;
    return HcpResponseDto.builder().matchingStatus(matchingStatus).data(hcpFoundList).build();
  }

  private void validateNameAndCountry(HcpRequestDto hcpDto) {
    // Check if at least firstName or lastName is provided
    if (!StringUtils.hasText(hcpDto.getFirstName()) && !StringUtils.hasText(hcpDto.getLastName())) {
      throw new CustomException(TYPE_MISSING_PARAM, MESSAGE_NAME_MISSING);
    }
    if (!StringUtils.hasText(hcpDto.getCountryISO2())) {
      throw new CustomException(TYPE_MISSING_PARAM, MESSAGE_COUNTRY_ISO2_MISSING);
    }
  }

  private List<Hcp> getGoodMatchedHcpListByLastName(List<Hcp> hcpList, HcpRequestDto hcpDto) {
    return hcpList.stream()
        .filter(
            hcp ->
                fuzzyMatchRatio(hcp.getLastName(), hcpDto.getLastName(), IGNORE_CASE)
                    >= FUZZY_RATIO)
        .toList();
  }

  private List<Hcp> getGoodMatchedHcpListByFirstName(List<Hcp> hcpList, HcpRequestDto hcpDto) {
    return hcpList.stream()
        .filter(
            hcp ->
                fuzzyMatchRatio(hcp.getFirstName(), hcpDto.getFirstName(), IGNORE_CASE)
                    >= FUZZY_RATIO)
        .toList();
  }

  private boolean checkIfSpecialtyCodeMatches(List<HcpDto> hcpFoundList, HcpRequestDto hcpDto) {
    return hcpFoundList.stream()
        .parallel()
        .filter(hcp -> hcp.getSpecialtyCode().equals(hcpDto.getSpecialtyCode()))
        .anyMatch(hcpFoundDto -> true);
  }

  private List<HcpDto> getTopListOfHcpFoundDto(List<Hcp> hcpListByCountry, HcpRequestDto hcpDto) {
    List<Hcp> sortedHcpList = getSortedHcpListBasedOnFullNameRatio(hcpListByCountry, hcpDto);
    List<String> specialtyCodes = sortedHcpList.stream().map(Hcp::getSpecialtyCode).toList();
    List<SpecialtyDto> specialties = specialtyService.getSpecialtiesByCodes(specialtyCodes);
    return HcpWithSpecialtyMapper.mapHcpAndSpecialtyDtoToHcpDto(sortedHcpList, specialties);
  }

  private List<Hcp> getSortedHcpListBasedOnFullNameRatio(List<Hcp> hcpList, HcpRequestDto hcpDto) {
    var sortedHcpList =
        new ArrayList<>(
            hcpList.stream()
                .map(hcp -> assignFuzzyMatchRatioToHcp(hcp, hcpDto))
                .sorted(
                    Comparator.comparing(Hcp::getRatio)
                        .reversed()) /*Sort by ratio in descending order.*/
                .limit(100)
                .toList());

    /* If the specialtyCode of a hcp matches the specialtyCode of the request param, then that Hcp
    will be at the top of the list. */
    var sortedHcpListWithSpecialtyMatched =
        sortedHcpList.stream() /* Extract objects where specialty code match found */
            .filter(hcp -> hcp.getSpecialtyCode().equals(hcpDto.getSpecialtyCode()))
            .toList();

    sortedHcpList.removeAll(sortedHcpListWithSpecialtyMatched);
    List<Hcp> finalList = new ArrayList<>(sortedHcpListWithSpecialtyMatched);
    finalList.addAll(sortedHcpList);
    return finalList;
  }

  private Hcp assignFuzzyMatchRatioToHcp(Hcp hcp, HcpRequestDto hcpDto) {
    // Calculate ratio based on firstName and lastName fuzzy match.
    double ratio =
        fuzzyMatchRatio(
            getFullName(hcp.getFirstName(), hcp.getLastName()),
            getFullName(hcpDto.getFirstName(), hcpDto.getLastName()),
            IGNORE_CASE);
    hcp.setRatio(ratio); // Set the calculated ratio to hcp object.
    return hcp;
  }

  private String getFullName(String firstName, String lastName) {
    firstName = firstName == null ? "" : firstName;
    lastName = lastName == null ? "" : lastName;
    String fullName = firstName + " " + lastName;
    return fullName.trim();
  }

  public HcpDto findHcpByOmniziaId(String omniziaId) {
    if (omniziaId != null) {
      return hcpService.getHcpByOmniziaId(omniziaId);
    }
    throw new CustomException(TYPE_MISSING_PARAM, MESSAGE_OMNIZIA_ID_IS_MISSING);
  }
}
