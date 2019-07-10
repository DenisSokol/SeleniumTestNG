package com.orangeHRM.app;

import com.orangeHRM.app.Page.PageMethods.CommonMethods;
import com.orangeHRM.app.Page.PageUtils.DriverFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.Properties;

public class BaseTest extends CommonMethods {

   @BeforeClass(groups = {"orangeHRM"})
   public void setUpClass() throws Exception {
      Properties prop = getProp();
      baseURL = prop.getProperty("base.url");
      login = getCredentials().get("login");
      password = getCredentials().get("password");
      log("[INFO] User login: " + login);
      log("[INFO] User password: " + password);
      log("[INFO] Log file path: " + logFile.getAbsolutePath());
   }

   @AfterClass(groups = {"orangeHRM"})
   public void tearDownClass() throws Exception {
      logOut();
      DriverFactory.getInstance().removeDriver();
   }
}