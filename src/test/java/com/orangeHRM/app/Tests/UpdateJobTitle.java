package com.orangeHRM.app.Tests;

import com.orangeHRM.app.BaseTest;
import com.orangeHRM.app.Page.PageUtils.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import static com.orangeHRM.app.Page.PageAssertions.*;

public class UpdateJobTitle extends BaseTest {
   @Test(groups = {"orangeHRM"})
   public void testJobTitleUpdate() throws Exception {
      driver = DriverFactory.getInstance().getDriver();
      boolean jobTitleCreated = false;
      boolean jobTitleUpdated = false;
      String newJobTitle = "V2 QA Engineer";
      String newJobDescription = "V2 Description";
      String newJobNote = "V2 Note";
      try {
         TestTitle("#3. Открыть страницу Job Titles (Admin -> Job -> Job Titles) и проверить, "
               + "что Job Title может быть создан (с полями: Job Title, Job Description,"
               + " Job Specification, Note) и изменён. ");
         driver.get(baseURL);
         logIn(login, password);
         createJobTitle("V1 QA Engineer", "V1 Description", "V1 Note");
         Thread.sleep(500);
         items = driver.findElements(By.cssSelector("tbody a"));
         itemsArray = items.toArray(new WebElement[items.size()]);
         for (WebElement item : itemsArray) {
            if (item.getText().equals("V1 QA Engineer")) {
               jobTitleCreated = true;
               item.click();
               elementClick(By.cssSelector(btnEdit));
               elementClear(By.cssSelector(fldJobTitle));
               elementSendKeys(By.cssSelector(fldJobTitle), newJobTitle);
               elementClear(By.cssSelector(fldJobDescription));
               elementSendKeys(By.cssSelector(fldJobDescription), newJobDescription);
               elementClear(By.cssSelector(fldJobNote));
               elementSendKeys(By.cssSelector(fldJobNote), newJobNote);
               elementClick(By.cssSelector(btnSave));
               break;
            }
         }
         pageAssertTrue(jobTitleCreated, "FAIL! Job Title Was NOT Created!");
         // checking the Job Title and Description were updated on Job Titles Table
         items = driver.findElements(By.cssSelector("tbody tr"));
         itemsArray = items.toArray(new WebElement[items.size()]);
         for (WebElement item : itemsArray) {
            if (item.findElement(By.cssSelector(" td.left:nth-of-type(2)")).getText().equals(newJobTitle)) {
               pageAssertEqual(newJobDescription, item.findElement(By.cssSelector(" td.left:nth-of-type(3)")).getText(),
                     "Fail! Job Description Was NOT Updated!");
               jobTitleUpdated = true;
               item.findElement(By.cssSelector(" input[type = checkbox]")).click();
               break;
            }
         }
         pageAssertTrue(jobTitleUpdated, "FAIL! Job Title Was NOT Created!");
      }
      catch (Exception e) {
         logClassError(new Object() {
         }.getClass(), e);
      }
      finally {
         if (jobTitleUpdated) {
            TestTitle("#2 Delete Job Title: " + newJobTitle);
            deleteJobTitle(newJobTitle);
         }
      }
   }
}