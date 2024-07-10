package com.omnizia.scrapinguniverse.dftbatchjob;

import com.omnizia.scrapinguniverse.dto.DigitalFootprintDto;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class DftItemProcessor implements ItemProcessor<DigitalFootprintDto, DigitalFootprintDto> {

  private final String className;

  public DftItemProcessor() {
    this.className = this.getClass().getSimpleName();
  }

  @Override
  public DigitalFootprintDto process(@NonNull DigitalFootprintDto item) {
    log.info("{}: Item Name : {}", className, item.getHcpName());
    return item;
  }
}
