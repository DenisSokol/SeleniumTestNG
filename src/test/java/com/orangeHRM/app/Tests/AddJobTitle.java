package com.orangeHRM.app.Tests;

import com.orangeHRM.app.BaseTest;
import com.orangeHRM.app.Page.PageUtils.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import static com.orangeHRM.app.Page.PageAssertions.*;

public class AddJobTitle extends BaseTest {
   @Test(groups = {"orangeHRM"})
   public void testJobTitleCreation() throws Exception {
      driver = DriverFactory.getInstance().getDriver();
      boolean jobTitleCreated = false;
      String jobTitle = "Test QA Engineer";
      String jobDescription = "Et ipsa scientia potestas est";
      String jobNote = "Aquila non captat muscas";
      try {
         TestTitle("#1. Открыть страницу Job Titles (Admin -> Job -> Job Titles) и проверить, "
               + "что Job Title может быть создан (с полями: Job Title, Job Description,"
               + " Job Specification, Note) и удалён. ");
         driver.get(baseURL);
         logIn(login, password);
         createJobTitle(jobTitle, jobDescription, jobNote);
         Thread.sleep(500);
         // searching for a created job title
         items = driver.findElements(By.cssSelector("tbody tr"));
         itemsArray = items.toArray(new WebElement[items.size()]);
         for (WebElement item : itemsArray) {
            if (item.findElement(By.cssSelector(" a")).getText().equals(jobTitle)) {
               jobTitleCreated = true;
               log("[INFO] Job Title: " + jobTitle + " Was Successfully Created");
               item.findElement(By.cssSelector(" input[type = checkbox]")).click();
               break;
            }
         }
         pageAssertTrue(jobTitleCreated, "FAIL! Job Title Was NOT Created!");
      }
      catch (Exception e) {
         logClassError(new Object() {
         }.getClass(), e);
      }
      finally {
         if (jobTitleCreated) {
            TestTitle("#2 Delete Job Title: " + jobTitle);
            deleteJobTitle(jobTitle);
         }
      }
   }
}
