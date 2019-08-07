package com.project.test.testrailintegration.core;

import java.io.IOException;
import java.util.Properties;
import org.testng.ISuite;

public class ProductProperties {

  private static String env = System.getProperty("env");
  private static Properties properties = new Properties();

  static {
    try {
      properties
          .load(ProductProperties.class.getClassLoader().getResourceAsStream("config.properties"));
    } catch (IOException e) {
      // We should fail immediately, we can't work without config.properties
      throw new RuntimeException(e);
    }
  }

  public static synchronized String initEnv(ISuite iSuite) {
    if (env == null) {
      env = iSuite.getXmlSuite().getParameter("Environment");
    }
    return env;
  }

  public static String getEnv() {
    return env;
  }

  public static void setEnv(String value) {
    env = value;
  }

  public static String getEnvProperty(String name) {
    return properties.getProperty(env + "." + name);
  }

  public static String getProperty(String name) {
    return properties.getProperty(name);
  }


}
