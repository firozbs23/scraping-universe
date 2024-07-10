package com.omnizia.scrapinguniverse.dftbatchjob;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import com.omnizia.scrapinguniverse.dbcontextholder.DataSourceContextHolder;
import com.omnizia.scrapinguniverse.dto.DigitalFootprintDto;
import com.omnizia.scrapinguniverse.dto.TrackTraceDto;
import com.omnizia.scrapinguniverse.service.DigitalFootprintsService;
import com.omnizia.scrapinguniverse.service.TrackTraceService;
import com.omnizia.scrapinguniverse.utils.StringUtils;
import com.omnizia.scrapinguniverse.utils.TimeUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;

@Slf4j
@Scope("step")
public class DftItemWriter implements ItemWriter<DigitalFootprintDto> {

  private final String className;
  private final String jobId;
  private final DigitalFootprintsService digitalFootprintsService;
  private final TrackTraceService trackTraceService;

  public DftItemWriter(
      @Value("#{jobParameters['jobId']}") String jobId,
      DigitalFootprintsService digitalFootprintsService,
      TrackTraceService trackTraceService) {
    this.digitalFootprintsService = digitalFootprintsService;
    this.trackTraceService = trackTraceService;
    this.jobId = jobId;
    className = this.getClass().getSimpleName();
    log.info("{}: Job id inside write: {}", className, jobId);
  }

  @Override
  public void write(@NonNull Chunk<? extends DigitalFootprintDto> chunk) {
    List<DigitalFootprintDto> digitalFootprintDtos = new ArrayList<>(chunk.getItems());
    log.info("{}: Chunk size : {}", className, digitalFootprintDtos.size());

    List<DigitalFootprintDto> updatedFootprints = getHcpStatus(digitalFootprintDtos);

    log.info("{}: Chunk size after status changed : {}", className, updatedFootprints.size());

    updateInVirtualThread(updatedFootprints);

    log.info("{}: Chunk data saved.", className);
  }

  private List<DigitalFootprintDto> getHcpStatus(List<DigitalFootprintDto> digitalFootprintDtos) {

    List<TrackTraceDto> hcpList =
        digitalFootprintDtos.stream()
            .filter(item -> StringUtils.isNotBlank(item.getHcpName()))
            .filter(item -> StringUtils.isValidURL(item.getHcpWebsite()))
            .map(
                item ->
                    TrackTraceDto.builder()
                        .hcpName(item.getHcpName())
                        .hcpWebsite(item.getHcpWebsite())
                        .dftId(item.getViqIdDigitalFootprint())
                        .build())
            .toList();

    List<TrackTraceDto> updatedTrackTraceDtos = trackTraceService.checkHcpsStatus(hcpList);

    Map<String, TrackTraceDto> trackTraceDtoMap =
        updatedTrackTraceDtos.stream()
            .collect(Collectors.toMap(TrackTraceDto::getDftId, obj -> obj));

    return digitalFootprintDtos.stream()
        .filter(item -> trackTraceDtoMap.containsKey(item.getViqIdDigitalFootprint()))
        .peek(
            obj -> {
              TrackTraceDto trackTraceDto = trackTraceDtoMap.get(obj.getViqIdDigitalFootprint());
              String oldStatus = StringUtils.getStringOrEmpty(obj.getTrackTraceStatus());
              String newStatus = StringUtils.getStringOrEmpty(trackTraceDto.getStatus());
              if (!oldStatus.equals(newStatus)) {
                obj.setTrackTraceStatusChanged(true);
                obj.setTrackTraceStatus(newStatus);
                obj.setUpdatedDate(TimeUtils.getCurrentTimeUTC());
                obj.setUpdatedByJob(jobId);
              }
            })
        .filter(DigitalFootprintDto::isTrackTraceStatusChanged)
        .peek(obj -> {})
        .toList();
  }

  private void updateInVirtualThread(List<DigitalFootprintDto> updatedFootprints) {
    Runnable task =
        () -> {
          try {
            DataSourceContextHolder.setDataSourceType("olam");
            digitalFootprintsService.updateDigitalFootprints(updatedFootprints, jobId);
            log.info("{}: Job updated successfully. JobId : {}", className, jobId);
          } catch (Exception e) {
            log.error("{} : Error in virtual thread", className, e);
          } finally {
            DataSourceContextHolder.clearDataSourceType();
          }
        };

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      Future<?> future = executor.submit(task);
      future.get(); // Wait for the task to complete
    } catch (ExecutionException | InterruptedException e) {
      log.error("{} : Execution error in virtual thread", className, e);
    }
  }
}
