package com.orangeHRM.app.Page.PageUtils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class DriverFactory {
   private static DriverFactory instance = new DriverFactory();

   public static DriverFactory getInstance() {
      return instance;
   }

   ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>() {
      @Override
      protected WebDriver initialValue() {
         WebDriver driver = null;
         String ChromePath;
         if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            ChromePath = "drivers/chromedriver";
         }
         else {
            ChromePath = "drivers/chromedriver.exe";
         }

         File file = new File(ChromePath);
         System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
         ChromeOptions options = new ChromeOptions();
         options.addArguments("disable-infobars");
         options.addArguments("disable-default-apps");
         options.addArguments("--no-sandbox");
         options.addArguments("--disable-dev-shm-usage");
         options.addArguments("disable-extensions");
         options.setExperimentalOption("useAutomationExtension", false);
         options.addArguments("window-size=5000,3000");
         options.addArguments("start-maximized");
         driver = new ChromeDriver(options);
         driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
         driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
         return driver;
      }
   };

   public WebDriver getDriver() {
      return driver.get();
   }

   public void removeDriver() {
      driver.get().quit();
      driver.remove();
   }
}