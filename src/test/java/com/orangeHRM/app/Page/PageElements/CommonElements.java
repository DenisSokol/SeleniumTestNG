package com.orangeHRM.app.Page.PageElements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CommonElements {

   // Page Variables
   public WebDriver driver;
   public List<WebElement> items;
   public WebElement[] itemsArray;
   public String baseURL;
   public String login;
   public String password;
   public static int threadCount;

   public String fldLoginEmail = "#txtUsername";
   public String fldLoginPassword = "#txtPassword";
   public String btnLoginSubmit = "#btnLogin";
   public String btnUserMenu = "#welcome";
   public String btnUserMenuLogout = "div#welcome-menu li:nth-of-type(2) a";
   public String btnAdminMenuModule = "#menu_admin_viewAdminModule";
   public String btnAdminMenuJob = "#menu_admin_Job";
   public String btnAdminMenuJobTitle = "#menu_admin_viewJobTitleList";
   public String btnAdd = "#btnAdd";
   public String fldJobTitle = "#jobTitle_jobTitle";
   public String fldJobDescription = "#jobTitle_jobDescription";
   public String fldJobNote = "#jobTitle_note";
   public String btnJobSpec = "#jobTitle_jobSpec";
   public String btnSave = "#btnSave";
   public String btnDelete = "#btnDelete";
   public String btnDeleteConfirm = "#dialogDeleteBtn";
   public String btnEdit = "#btnSave";
}

