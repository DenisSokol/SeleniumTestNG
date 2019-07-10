package com.orangeHRM.app.Page;

import java.io.IOException;

import static org.testng.Assert.*;

public class PageAssertions {
   public static void pageAssertTrue(boolean element, String message) throws IOException {
      try {
         assertTrue(element, message);
      }
      catch (Throwable e) {
         throw new RuntimeException(e.getMessage());
      }
   }

   public static void pageAssertFalse(boolean element, String message) throws IOException {
      try {
         assertFalse(element, message);
      }
      catch (Throwable e) {
         throw new RuntimeException(e.getMessage());
      }
   }

   public static void pageAssertEqual(String expected, String actual, String message) throws IOException {
      try {
         assertEquals(expected, actual, message);
      }
      catch (Throwable e) {
         throw new RuntimeException(e.getMessage());
      }
   }
}