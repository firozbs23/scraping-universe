package com.omnizia.scrapinguniverse.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Scraping Universe API")
                .version("1.0")
                .description("This API is used for fuzzy matching and Track-And-Trace"))
        .servers(
            List.of(
                new Server().url("https://fuzzy-matching.omnizia.com").description("Dev server"),
                new Server().url("https://fuzzy-matching.bscdp.com").description("Dev server"),
                new Server().url("http://localhost:8080").description("Local server")));
  }
}
