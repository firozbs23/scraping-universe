package com.omnizia.scrapinguniverse.dftbatchjob;

import com.omnizia.scrapinguniverse.dbcontextholder.DataSourceContextHolder;
import com.omnizia.scrapinguniverse.dto.DigitalFootprintDto;
import com.omnizia.scrapinguniverse.service.DigitalFootprintsService;
import com.omnizia.scrapinguniverse.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class DftItemReader implements ItemReader<DigitalFootprintDto> {

  private Iterator<DigitalFootprintDto> dataIterator;
  private final String className;
  private final DigitalFootprintsService digitalFootprintsService;

  public DftItemReader(DigitalFootprintsService digitalFootprintsService) {
    this.digitalFootprintsService = digitalFootprintsService;
    this.className = this.getClass().getSimpleName();
  }

  @Override
  public DigitalFootprintDto read() {
    String currentTime = TimeUtils.getCurrentTimeUTC();

    if (dataIterator == null) {
      List<DigitalFootprintDto> footprintsDto;
      try {
        footprintsDto = getFromVirtualThread();
      } catch (ExecutionException | InterruptedException e) {
        log.error("{} : Failed to read digital footprints data in {}", className, currentTime);
        throw new RuntimeException(e);
      }

      if (footprintsDto != null) {
        dataIterator = footprintsDto.iterator();
        log.info("{}: Read item size : {}", className, footprintsDto.size());
      } else log.info("{}: Not able to read item.", className);
    }

    if (dataIterator.hasNext()) {
      return dataIterator.next();
    } else {
      return null;
    }
  }

  private List<DigitalFootprintDto> getFromVirtualThread()
      throws ExecutionException, InterruptedException {
    Callable<List<DigitalFootprintDto>> task =
        () -> {
          try {
            DataSourceContextHolder.setDataSourceType("olam");
            return digitalFootprintsService.getDigitalFootprintsDto();
          } finally {
            DataSourceContextHolder.clearDataSourceType();
          }
        };

    try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
      Future<List<DigitalFootprintDto>> future = executor.submit(task);
      return future.get(); // Wait for the result and return it
    }
  }
}
