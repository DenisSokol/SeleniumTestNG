package com.orangeHRM.app.Page.PageMethods;

import com.orangeHRM.app.Page.PageElements.CommonElements;
import com.orangeHRM.app.Page.PageUtils.DriverFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.io.*;
import java.util.*;
import java.time.Duration;

import static com.orangeHRM.app.Page.PageAssertions.*;

public class CommonMethods extends CommonElements {

   public void TestTitle(String title) throws IOException {
      log("--------------------------------\nTEST: " + title + "\n--------------------------------");
   }

   public static boolean isDisplayedXpath(String xpath) throws Exception {
      WebDriver driver = DriverFactory.getInstance().getDriver();
      boolean display = false;
      try {
         display = driver.findElement(By.xpath(xpath)).isDisplayed();
      }
      catch (NoSuchElementException e) {
         log("[WARNING] " + e.getMessage().split("\n")[0]);
      }
      return display;
   }

   public static boolean isDisplayedCss(String cssSelector) throws Exception {
      WebDriver driver = DriverFactory.getInstance().getDriver();
      boolean display = false;
      try {
         display = driver.findElement(By.cssSelector(cssSelector)).isDisplayed();
      }
      catch (NoSuchElementException e) {
         log("[WARNING] " + e.getMessage().split("\n")[0]);
      }
      return display;
   }


   public void waitForElement(By by, int maxtime) throws Exception {
      log("[INFO] Waiting for element " + by.toString());
      Wait<WebDriver> wait = new FluentWait<>(driver)
            .withTimeout(Duration.ofSeconds(maxtime))
            .pollingEvery(Duration.ofMillis(500))
            .ignoring(NoSuchElementException.class);
      try {
         Thread.sleep(500);
         wait.until(ExpectedConditions.visibilityOfElementLocated(by));
         wait.until(ExpectedConditions.elementToBeClickable(by));
      }
      catch (TimeoutException e) {
         log("--------------------------------\n[ERROR] "
               + "Element (" + by.toString() + ") is not loaded!\n--------------------------------");
         Screenshot();
         throw new RuntimeException("\n[ERROR] Element (" + by.toString() + ") is not loaded!\n" + e);
      }
   }


   public static void Screenshot() throws IOException {
      WebDriver driver = DriverFactory.getInstance().getDriver();
      long timestamp = new Date().getTime();
      String file = timestamp + ".jpg";
      File scrTempFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
      File scrFile = new File("Screenshots/" + file);
      FileUtils.copyFile(scrTempFile, scrFile);
      if (System.getenv("JENKINS_URL") != null) {
         log("[INFO] Screenshot: " + System.getenv("JENKINS_URL") + "job/"
               + System.getenv("JOB_NAME").replace(" ", "%20") + "/ws/Screenshots/" + file);
      }
      else {
         log("[INFO] Screenshot: " + scrFile.getAbsolutePath());
      }
   }


   public void logIn(String userID, String password) throws Exception {
      log("URL: " + baseURL);
      driver.get(baseURL);
      if (isDisplayedCss(fldLoginEmail)) {
         log("[INFO] Logging in...\n--------------------------------");
         elementClick(By.cssSelector(fldLoginEmail));
         elementSendKeys(By.cssSelector(fldLoginEmail), userID);
         pageAssertTrue(isDisplayedCss(fldLoginPassword), "Can't find Password text field.");
         elementClick(By.cssSelector(fldLoginPassword));
         elementSendKeys(By.cssSelector(fldLoginPassword), password);
         pageAssertTrue(isDisplayedCss(btnLoginSubmit), "Can't find Sign In button.");
         elementClick(By.cssSelector(btnLoginSubmit));
      }
   }

   public void logOut() throws Exception {
      driver = DriverFactory.getInstance().getDriver();
      driver.get(baseURL);
      try {
         if (isDisplayedCss(btnUserMenu)) {
            log("--------------------------------\n[INFO] Logging out...");
            elementClick(By.cssSelector(btnUserMenu));
            waitForElement(By.cssSelector(btnUserMenuLogout), 5);
            elementClick(By.cssSelector(btnUserMenuLogout));
            waitForElement(By.cssSelector(fldLoginEmail), 15);
            pageAssertTrue(isDisplayedCss(fldLoginEmail), "LogOut Unsuccessful");
         }
      }
      catch (Exception e) {
         log("[WARNING] " + e.getMessage().split("\n")[0]);
      }
   }


   public static File logFile() {
      new File("logs").mkdirs();
      long timestamp = new Date().getTime();
      String path = "logs/" + timestamp + "_log.txt";
      return new File(path);
   }

   public static File logFile = logFile();

   public static void log(String message) throws IOException {
      String[] logMsgs = message.split("\n");
      PrintWriter out = new PrintWriter(new FileWriter(logFile, true), true);
      for (String msg : logMsgs) {
         if (threadCount > 1) {
            System.out.println("[" + (Thread.currentThread().getId() % threadCount + 1) + "]" + msg);
            out.write("[" + (Thread.currentThread().getId() % threadCount + 1) + "]" + msg + "\n");
         }
         else {
            System.out.println(msg);
            out.write(msg + "\n");
         }
      }
      out.close();
   }

   public Map<String, String> getCredentials() throws Exception {
      Map<String, String> map = new HashMap<String, String>();
      File file = new File("local.properties");
      if (file.exists()) {
         FileInputStream fis = new FileInputStream(file);
         Properties prop = new Properties();
         prop.load(fis);
         login = prop.getProperty("login");
         password = prop.getProperty("password");
         map.put("login", login);
         map.put("password", password);
      }
      else {
         map.put("login", "Admin");
         map.put("password", "admin123");
      }
      return map;
   }

   public void elementClick(By by) throws Exception {
      log("[INFO] Clicking element " + by.toString());
      try {
         driver.findElement(by).click();
      }
      catch (Exception e) {
         if (e.getMessage().contains("element is not attached")) {
            int count = 0;
            boolean ignore = true;
            while (ignore && count < 5) {
               Thread.sleep(1000);
               try {
                  driver.findElement(by).click();
                  break;
               }
               catch (Exception staleE) {
                  ignore = staleE.getMessage().contains("element is not attached");
                  count++;
                  if (!ignore || count == 5) {
                     logError("Can't click on element " + by.toString(), staleE);
                  }
               }
            }
         }
         else {
            logError("Can't click on element " + by.toString(), e);
         }
      }
   }


   public void elementClear(By by) throws Exception {
      log("[INFO] Clearing element " + by.toString());
      try {
         driver.findElement(by).clear();
      }
      catch (Exception e) {
         logError("Can't clear element " + by.toString(), e);
      }
   }

   public void elementSendKeys(By by, String text) throws Exception {
      log("[INFO] Entering text '" + text + "' " + by.toString());
      try {
         driver.findElement(by).sendKeys(text);
      }
      catch (Exception e) {
         logError("Can't send keys to element " + by.toString(), e);
      }
   }


   public static void logError(String testName, Exception e) throws Exception {
      Screenshot();
      String[] ss = ExceptionUtils.getRootCauseStackTrace(e);
      log("---------- ERRROR LOG ----------\nFailed test: " + testName);
      log(StringUtils.join(ss, "\n"));
      log("---------- END ERRROR LOG ----------");
      throw new Exception("[ERROR] Failed test: " + testName);
   }

   public static void logClassError(Class testClass, Exception e) throws Exception {
      Screenshot();
      String className = testClass.getEnclosingClass().getSimpleName();
      String methodName = testClass.getEnclosingMethod().getName();
      String[] ss = ExceptionUtils.getRootCauseStackTrace(e);
      if (Arrays.toString(ss).contains("com.orangeHRM.app")) {
         ArrayList<String> arrlist = new ArrayList<String>();
         arrlist.add(ss[0]);
         for (String s : ss) {
            if (s.contains("com.orangeHRM.app")) {
               arrlist.add(s);
            }
         }
         ss = arrlist.toArray(new String[0]);
      }
      log("---------- ERRROR LOG ----------\n[FAILURE] Failed test: " + className + ":" + methodName);
      log(StringUtils.join(ss, "\n"));
      log("---------- END ERRROR LOG ----------");
      throw new Exception("[FAILURE] Failed test: " + className + ":" + methodName);
   }

   public Properties getProp() throws Exception {
      InputStream iStream = this.getClass().getResourceAsStream("/environment.properties");
      Properties prop = new Properties();
      prop.load(iStream);
      return prop;
   }

   public void createJobTitle(String jobTitle, String jobDescription, String jobNote) throws Exception {
      Actions action = new Actions(driver);
      WebElement adminModule = driver.findElement(By.cssSelector(btnAdminMenuModule));
      WebElement adminModuleJob = driver.findElement(By.cssSelector(btnAdminMenuJob));
      action.moveToElement(adminModule).moveToElement(adminModuleJob).moveToElement(driver.findElement(By.cssSelector(btnAdminMenuJobTitle))).click().build().perform();
      elementClick(By.cssSelector(btnAdd));
      elementSendKeys(By.cssSelector(fldJobTitle), jobTitle);
      elementSendKeys(By.cssSelector(fldJobDescription), jobDescription);
      elementSendKeys(By.cssSelector(fldJobNote), jobNote);
      WebElement uploadElement = driver.findElement(By.cssSelector(btnJobSpec));
      uploadElement.sendKeys(System.getProperty("user.dir") + "/src/test/java/com/orangeHRM/app/Page/Data/jobSpecification.txt");
      elementClick(By.cssSelector(btnSave));

   }

   public void deleteJobTitle(String jobTitle) throws Exception {
      elementClick(By.cssSelector(btnDelete));
      elementClick(By.cssSelector(btnDeleteConfirm));
      items = driver.findElements(By.cssSelector("tbody a"));
      itemsArray = items.toArray(new WebElement[items.size()]);
      for (WebElement item : itemsArray) {
         pageAssertFalse(item.getText().equals(jobTitle), "FAIL! Job Title Was NOT Deleted!");
      }
   }
}

