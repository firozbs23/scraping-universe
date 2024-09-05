package com.omnizia.scrapinguniverse.config;

import com.omnizia.scrapinguniverse.dbcontextholder.DataSourceContextHolder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@EntityScan(basePackages = "com.omnizia.scrapinguniverse.entity")
@EnableJpaRepositories(
    basePackages = "com.omnizia.scrapinguniverse.repository",
    entityManagerFactoryRef = "entityManagerFactory",
    transactionManagerRef = "transactionManager")
public class DataSourceConfig {

  @Bean(name = "mcd")
  public DataSource dataSource1() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setUrl(
        "jdbc:postgresql://ls-e7500498eb7cca7cb3501ee037e5841634bfe0d3.cngn4h200lce.eu-central-1.rds.amazonaws.com:5432/brainstation_crdlp_dev");
    dataSource.setUsername("cdp_dev_user");
    dataSource.setPassword("PhAL5XwyWm");
    dataSource.setSchema("gds");
    dataSource.setDriverClassName("org.postgresql.Driver");
    return dataSource;
  }

  @Bean(name = "olam")
  public DataSource dataSource2() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setUrl(
        "jdbc:postgresql://ls-e7500498eb7cca7cb3501ee037e5841634bfe0d3.cngn4h200lce.eu-central-1.rds.amazonaws.com:5432/crdlp_dev");
    dataSource.setUsername("cdp_dev_user");
    dataSource.setPassword("PhAL5XwyWm");
    dataSource.setSchema("gds");
    dataSource.setDriverClassName("org.postgresql.Driver");
    return dataSource;
  }

  @Bean(name = "springworks")
  public DataSource dataSource3() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setUrl(
        "jdbc:postgresql://ls-4a50e17e2bb977d7821902c0814979441b391918.cngn4h200lce.eu-central-1.rds.amazonaws.com:5432/springworks_crdlp");
    dataSource.setUsername("omnizia_user");
    dataSource.setPassword("zXN!n!kAWA2mlL6S");
    dataSource.setSchema("gds");
    dataSource.setDriverClassName("org.postgresql.Driver");
    return dataSource;
  }

  @Bean(name = "brainstation")
  public DataSource dataSource4() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setUrl(
        "jdbc:postgresql://ls-e7500498eb7cca7cb3501ee037e5841634bfe0d3.cngn4h200lce.eu-central-1.rds.amazonaws.com:5432/crdlp_dev");
    dataSource.setUsername("cdp_dev_user");
    dataSource.setPassword("PhAL5XwyWm");
    dataSource.setSchema("gds");
    dataSource.setDriverClassName("org.postgresql.Driver");
    return dataSource;
  }

  @Bean(name = "recordati")
  public DataSource dataSource5() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setUrl(
        "jdbc:postgresql://ls-1a861009034a415ae969ba94d7877217e61e05be.cngn4h200lce.eu-central-1.rds.amazonaws.com:5432/recordati_crdlp");
    dataSource.setUsername("omnizia_user");
    dataSource.setPassword("u6F@RTeK4ZVbttHo");
    dataSource.setSchema("gds");
    dataSource.setDriverClassName("org.postgresql.Driver");
    return dataSource;
  }

  @Bean(name = "dataSource")
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setUrl("jdbc:postgresql://scraping-job-config-db:5432/spring_batch_db");
    // dataSource.setUrl("jdbc:postgresql://localhost:5434/spring_batch_db");
    dataSource.setUsername("username");
    dataSource.setPassword("password");
    dataSource.setDriverClassName("org.postgresql.Driver");

    // Execute SQL script
    DatabasePopulatorUtils.execute(createDatabasePopulator(), dataSource);

    return dataSource;
  }

  private DatabasePopulator createDatabasePopulator() {
    ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
    /*databasePopulator.addScript(
    new ClassPathResource("org/springframework/batch/core/schema-postgresql.sql"));*/
    databasePopulator.addScript(new ClassPathResource("db/batch_job.sql"));
    return databasePopulator;
  }

  @Primary
  @Bean(name = "dynamicDataSource")
  public DataSource routingDataSource(
      @Qualifier("mcd") DataSource dataSource1,
      @Qualifier("olam") DataSource dataSource2,
      @Qualifier("springworks") DataSource dataSource3,
      @Qualifier("brainstation") DataSource dataSource4,
      @Qualifier("recordati") DataSource dataSource5,
      @Qualifier("dataSource") DataSource batchJob) {
    AbstractRoutingDataSource routingDataSource =
        new AbstractRoutingDataSource() {
          @Override
          protected Object determineCurrentLookupKey() {
            return DataSourceContextHolder.getDataSourceType();
          }
        };

    Map<Object, Object> dataSourceMap = new HashMap<>();
    dataSourceMap.put("mcd", dataSource1);
    dataSourceMap.put("olam", dataSource2);
    dataSourceMap.put("springworks", dataSource3);
    dataSourceMap.put("brainstation", dataSource4);
    dataSourceMap.put("recordati", dataSource5);
    dataSourceMap.put("dataSource", batchJob);

    routingDataSource.setTargetDataSources(dataSourceMap);
    routingDataSource.setDefaultTargetDataSource(batchJob);

    return routingDataSource;
  }

  @Primary
  @Bean(name = "entityManagerFactory")
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(
      EntityManagerFactoryBuilder builder, @Qualifier("dynamicDataSource") DataSource dataSource) {
    Map<String, String> properties = new HashMap<>();
    properties.put("hibernate.hbm2ddl.auto", "none");

    return builder
        .dataSource(dataSource)
        .packages("com.omnizia.scrapinguniverse.entity")
        .persistenceUnit("default")
        .properties(properties)
        .build();
  }

  @Bean(name = "transactionManager")
  public PlatformTransactionManager transactionManager(
      @Qualifier("entityManagerFactory")
          LocalContainerEntityManagerFactoryBean entityManagerFactory) {
    return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactory.getObject()));
  }
}
