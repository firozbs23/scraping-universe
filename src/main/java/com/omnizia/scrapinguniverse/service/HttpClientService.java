package com.omnizia.scrapinguniverse.service;

import com.omnizia.scrapinguniverse.utils.SSLUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Slf4j
@Service
public class HttpClientService {

  private static final Duration TIMEOUT = Duration.ofSeconds(180);

  private static HttpClient createHttpClient() throws Exception {
    SSLContext sslContext = SSLUtils.createTrustAllSSLContext();
    return HttpClient.newBuilder().sslContext(sslContext).connectTimeout(TIMEOUT).build();
  }

  private static HttpClient httpClient;

  static {
    try {
      httpClient = createHttpClient();
    } catch (Exception e) {
      log.error("Failed to create HttpClient with custom SSL context", e);
    }
  }

  public HttpResponse<String> getPageSource(String urlString)
      throws URISyntaxException, IOException, InterruptedException {
    HttpRequest request =
        HttpRequest.newBuilder().uri(new URI(urlString)).GET().timeout(TIMEOUT).build();

    return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
  }
}
