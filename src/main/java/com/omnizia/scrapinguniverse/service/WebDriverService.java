package com.omnizia.scrapinguniverse.service;

import java.io.File;
import java.time.Duration;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;
import org.openqa.selenium.WebElement;

@Slf4j
@Service
public class WebDriverService {

  private static final String chromeDriverPath = "/app/chromedriver-linux64/chromedriver";
  // private static final String chromeDriverPath = "/home/bs00689/Documents/delete-me/chromedriver-linux64/chromedriver";
  private static final String chromeBinaryPath = "/app/chrome-linux64/chrome";
  //private static final String chromeBinaryPath = "/home/bs00689/Documents/delete-me/chrome-linux64/chrome";

  public boolean isTextPresent(String websiteUrl, String searchText) {
    WebDriver driver = null;

    searchText = searchText.trim().toLowerCase();

    try {
      log.info("Starting ChromeDriver...");
      driver = new ChromeDriver(getChromeDriverService(), getChromeOptions());
      driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(120));
      driver.get(websiteUrl);
      String title = driver.getTitle();

      WebElement body = driver.findElement(By.tagName("body"));
      log.info("Page title: {}", title);
      // Check if the body contains the text
      if (body.getText().trim().toLowerCase().contains(searchText)) return true;
      else {
        boolean isCaptcha = isLikelyCaptchaPage(body.getText().trim());
        if (isCaptcha){
          log.info("Captcha found");
        }
      }

    } catch (Exception e) {
      log.error("Error occurred while fetching page source", e);
    } finally {
      if (driver != null) {
        log.info("Quitting ChromeDriver...");
        driver.quit();
      }
    }
    return false;
  }

  private ChromeDriverService getChromeDriverService() {
    log.info("Setting system property for ChromeDriver: {}", chromeDriverPath);
    System.setProperty("webdriver.chrome.driver", chromeDriverPath);
    return new ChromeDriverService.Builder()
        .usingDriverExecutable(new File(chromeDriverPath))
        .usingAnyFreePort()
        .build();
  }

  private ChromeOptions getChromeOptions() {
    log.info("Setting Chrome options with binary: {}", chromeBinaryPath);
    ChromeOptions options = new ChromeOptions();
    options.setBinary(chromeBinaryPath);
    options.addArguments("--headless");
    options.addArguments("--window-size=1920,1080");
    options.addArguments("--disable-gpu");
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");
    options.addArguments("--remote-allow-origins=*");
    return options;
  }

  private boolean isLikelyCaptchaPage(String html) {
    // Example patterns to detect CAPTCHA pages
    String[] captchaIndicators = {
            "captcha",
            "g-recaptcha",
            "Please verify",
            "I'm not a robot",
            "not a robot",
            "recaptcha",
            "captcha",
            "challenge",
            "verification",
            "challenge-form"
    };

    for (String pattern : captchaIndicators) {
      if (html.toLowerCase().contains(pattern.toLowerCase())) {
        return true;
      }
    }

    return false;
  }
}
