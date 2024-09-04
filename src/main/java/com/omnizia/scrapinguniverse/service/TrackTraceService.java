package com.omnizia.scrapinguniverse.service;

import java.net.http.HttpResponse;
import java.net.URISyntaxException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.omnizia.scrapinguniverse.dto.TrackTraceDto;
import com.omnizia.scrapinguniverse.utils.StringUtils;
import com.omnizia.scrapinguniverse.utils.TimeUtils;
import com.omnizia.scrapinguniverse.utils.Utils;

import static com.omnizia.scrapinguniverse.utils.Constants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrackTraceService {

  private final HttpClientService httpClientService;
  private final WebDriverService webDriverService;

  private static final int MAX_RETRIES = 3;

  public Object getStatus(Object requestBody) {
    String currentTime = TimeUtils.getCurrentTimeUTC();
    log.info("Getting HCP track trace status. Time : {}", currentTime);
    List<TrackTraceDto> hcpList = new ArrayList<>();
    if (requestBody instanceof List<?> requestBodyList) {
      for (Object item : requestBodyList) {
        String hcpName = ((LinkedHashMap<?, ?>) item).get(HCP_NAME).toString();
        String hcpWebsite = ((LinkedHashMap<?, ?>) item).get(HCP_WEBSITE).toString();
        String dftId;
        try{
           dftId = ((LinkedHashMap<?, ?>) item).get(DFT_ID).toString();
        }catch (NullPointerException e){
          dftId = "";
        }

        TrackTraceDto traceDto =
            TrackTraceDto.builder().hcpName(hcpName).hcpWebsite(hcpWebsite).dftId(dftId).build();
        hcpList.add(traceDto);
      }

      currentTime = TimeUtils.getCurrentTimeUTC();
      log.info("Returning HCP track trace status list. Time : {}", currentTime);
      return checkHcpsStatus(hcpList);
    } else {

      String hcpName = ((LinkedHashMap<?, ?>) requestBody).get(HCP_NAME).toString();
      String hcpWebsite = ((LinkedHashMap<?, ?>) requestBody).get(HCP_WEBSITE).toString();
      String dftId;
      try {
        dftId = ((LinkedHashMap<?, ?>) requestBody).get(DFT_ID).toString();
      } catch (NullPointerException e) {
        dftId = "";
      }

      TrackTraceDto traceDto =
          TrackTraceDto.builder().hcpName(hcpName).hcpWebsite(hcpWebsite).dftId(dftId).build();
      hcpList.add(traceDto);

      List<TrackTraceDto> trackAndTraceDtos = checkHcpsStatus(hcpList);

      log.info("{} : Returning HCP track trace status.", this.getClass().getSimpleName());
      return trackAndTraceDtos.getFirst();
    }
  }

  public List<TrackTraceDto> checkHcpsStatus(List<TrackTraceDto> hcpList) {
    List<TrackTraceDto> responseData = new ArrayList<>();

    // Create an ExecutorService with virtual threads
    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
      List<Future<TrackTraceDto>> futures = new ArrayList<>();

      for (TrackTraceDto hcp : hcpList) {
        futures.add(executor.submit(createTask(hcp)));
      }

      // Collect results
      for (Future<TrackTraceDto> future : futures) {
        try {
          TrackTraceDto result = future.get();
          if (result != null) responseData.add(result);
        } catch (Exception e) {
          log.error("Error retrieving HCP status result. Exception: ", e);
        }
      }

    } catch (Exception e) {
      log.error("Error processing HCP status. Exception: ", e);
    }

    return responseData;
  }

  private Callable<TrackTraceDto> createTask(TrackTraceDto hcp) {
    return () -> {
      TrackTraceDto result = null;
      for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
        result = checkHcpStatus(hcp);
        if (!"Not Accessible".equalsIgnoreCase(result.getStatus())
            && !"Not Available".equalsIgnoreCase(result.getStatus())) {
          break;
        } else if ("Not Available".equalsIgnoreCase(result.getStatus())) {
          Thread.sleep(1000); // Wait for a short duration before retrying
        }
      }
      return result;
    };
  }

  private TrackTraceDto checkHcpStatus(TrackTraceDto trace) {
    String hcpName = StringUtils.removeExtraWhitespace(trace.getHcpName().trim());

    try {
      HttpResponse<String> response = httpClientService.getPageSource(trace.getHcpWebsite());

      if (response.statusCode() == 200) {
        String pageSource = StringUtils.removeExtraWhitespace(response.body().trim().toLowerCase());
        boolean isFound = pageSource.contains(hcpName.toLowerCase());
        trace.setStatus(isFound ? "Found" : "Not Found");
      } else {
        trace.setStatus("Not Available");
      }
    } catch (URISyntaxException e) {
      log.error("Invalid URL syntax for HCP website : {}", trace.getHcpWebsite(), e);
      trace.setStatus("Not Accessible");
    } catch (IOException | InterruptedException e) {
      log.error("Error accessing HCP website : {}", trace.getHcpWebsite(), e);
      trace.setStatus("Not Accessible");
    } catch (Exception e) {
      log.error("Unexpected error : {}", trace.getHcpWebsite(), e);
      trace.setStatus("Not Available");
    }

    if (!trace.getStatus().equals("Found")) { // If not found in first approach
      boolean isPresent = webDriverService.isTextPresent(trace.getHcpWebsite(), trace.getHcpName());

      if (isPresent) trace.setStatus("Found");
    }

    trace.setTimestamp(Utils.getCurrentTimeUTC());
    return trace;
  }
}
