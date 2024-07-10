package com.omnizia.scrapinguniverse.config;

import com.omnizia.scrapinguniverse.dftbatchjob.DftItemProcessor;
import com.omnizia.scrapinguniverse.dftbatchjob.DftItemReader;
import com.omnizia.scrapinguniverse.dftbatchjob.DftItemWriter;
import com.omnizia.scrapinguniverse.dto.DigitalFootprintDto;
import com.omnizia.scrapinguniverse.service.DigitalFootprintsService;
import com.omnizia.scrapinguniverse.service.TrackTraceService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class DftBatchJobConfig {

  private final JobRepository jobRepository;
  private final PlatformTransactionManager platformTransactionManager;
  private final DigitalFootprintsService digitalFootprintsService;
  private final TrackTraceService trackTraceService;

  @Bean
  public Job processJob(Step step) {
    return new JobBuilder("processJob", jobRepository).start(step).build();
  }

  @Bean
  public Step step(
      ItemReader<DigitalFootprintDto> reader,
      ItemProcessor<DigitalFootprintDto, DigitalFootprintDto> processor,
      ItemWriter<DigitalFootprintDto> writer) {
    return new StepBuilder("step", jobRepository)
        .<DigitalFootprintDto, DigitalFootprintDto>chunk(100, platformTransactionManager)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();
  }

  @Bean
  @StepScope
  public ItemReader<DigitalFootprintDto> reader() {
    return new DftItemReader(digitalFootprintsService);
  }

  @Bean
  public ItemProcessor<DigitalFootprintDto, DigitalFootprintDto> processor() {
    return new DftItemProcessor();
  }

  @Bean
  @StepScope
  public ItemWriter<DigitalFootprintDto> writer(@Value("#{jobParameters['jobId']}") String jobId) {
    return new DftItemWriter(jobId, digitalFootprintsService, trackTraceService);
  }
}
