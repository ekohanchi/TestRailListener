package com.project.test.testrailintegration.core;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.Reporter;

public final class LogUtilities {

  /**
   * The instance of the Utilities. <p> Note: Make this a Singleton.
   */
  private static LogUtilities instance = null;

  @SuppressWarnings("unused")
private final static int CHARCOUNT = 256;

  /**
   * Constructor for Utilities <p> Constructor to prevent instantiation from other classes <p> Note:
   * Make this a Singleton.
   */
  private LogUtilities() {
    // Exists only to defeat instantiation.
	  String log4jConfPath = "src/main/resources/log4j.properties";
	  PropertyConfigurator.configure(log4jConfPath);
  }

  /**
   * Utilities getInstance() Returns the Utilities instance <p> The method returns member variable
   * <i>instance</i>, instantiating the variable if it instantiated wasn't already.
   *
   * Note: Make this a Singleton.
   *
   * @return {@link LogUtilities} - the instance
   */
  public static synchronized LogUtilities getInstance() {
    if (instance == null) {
      instance = new LogUtilities();
    }
    return instance;
  }

  /**
   * The message logger for this class.
   */
  private static Logger logger = Logger.getLogger(LogUtilities.class);

/**
* void logInfoMessage(String msg) <p> Logs the message. <p> The method gives the member
* <i>logger</i> the message <i>msg</i>, then logs it through the report.
*
* @param msg - {@link String} - the message to log
*/
public static void logInfoMessage(String msg) {
 logger.info(msg);
 Reporter.log(msg);
 CommentReporter.addComment(msg);
}
  
  /**
   * void logWarningMessage(String msg) <p> Logs the warning message. <p> The method gives the
   * member <i>logger</i> the message <i>msg</i>, then logs it through the report.
   *
   * @param msg - {@link String} - the warning message to log
   */
  public static void logWarningMessage(String msg) {
    logger.warn(msg);
    Reporter.log(msg);
  }

  /**
   * void logSevereMessage(String msg) <p> Logs the severe message. <p> The method gives the member
   * <i>logger</i> the message <i>msg</i>, then logs it through the report.
   *
   * @param msg - {@link String} - the severe message to log
   */
  public static void logSevereMessage(String msg) {
    logWarningMessage(msg);
  }

}
