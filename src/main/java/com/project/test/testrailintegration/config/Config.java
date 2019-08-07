package com.project.test.testrailintegration.config;

import java.util.Properties;

import com.project.test.testrailintegration.TestRailListener;
import com.project.test.testrailintegration.api.client.method.ConfigsRequest;
import com.project.test.testrailintegration.core.LogUtilities;

public class Config {

  private static Properties prop = new Properties();
  private static boolean initComplete = false;
  private static boolean initSuccessful = false;
  private static String reportListenerPropertiesFile = "TestRailListener.properties";
  private static String propertyUserEmail = "TestRail.User.Email";
  private static String propertyUserApiKey = "TestRail.User.APIKEY";
  private static String propertyProjectID = "TestRail.ProjectID";
  public static final String PROTOCOL = "https";
  public static final String HOST = "";
  public static final int PORT = 443;
  public static final String PATH = "index.php";
  public static final String API_PATH = "/api/v2/";
  public static String USER;
  public static String API_KEY;
  public static String PROJECT_ID;

  public static void init() {
    if (!initComplete) {
      initComplete = true;
      try {
        prop.load(TestRailListener.class.getClassLoader()
            .getResourceAsStream(reportListenerPropertiesFile));
        USER = prop.getProperty(propertyUserEmail);
        API_KEY = prop.getProperty(propertyUserApiKey);
        PROJECT_ID = prop.getProperty(propertyProjectID);
        if (!new ConfigsRequest().isHealthCheckPassed()) {
          LogUtilities.logWarningMessage(String.format(
              "Authentication failed, check your '" + reportListenerPropertiesFile + "' settings. Now we have "
                  + propertyUserEmail + "=%s, " + propertyUserApiKey + "=%s, " + propertyProjectID + "=%s", USER, API_KEY,
              PROJECT_ID));
          return;
        }
        initSuccessful = true;
      } catch (Exception e) {
        LogUtilities.logWarningMessage(
            "Unable to load 'ReporterListener.properties' please check that this file in your classpath. "
                + "TestRailReportListener will be disabled.");
      }
    }
  }

  public static boolean isListenerEnabled() {
    return initSuccessful;
  }


}
