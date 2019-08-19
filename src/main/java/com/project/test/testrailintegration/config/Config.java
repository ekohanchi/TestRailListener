package com.project.test.testrailintegration.config;

import java.util.Properties;

import com.project.test.testrailintegration.TestRailListener;
import com.project.test.testrailintegration.api.client.method.ConfigsRequest;
import com.project.test.testrailintegration.core.LogUtilities;

public class Config {
	private static Properties prop1 = new Properties();
	private static Properties prop2 = new Properties();
	private static boolean initComplete = false;
	private static boolean initSuccessful = false;
	private static String testRailCommonPropertiesFile = "TestRailCommon.properties";
	private static String propertyProtocol = "Protocol";
	private static String propertyHost = "Host";
	private static String propertyPort = "Port";
	private static String propertyPath = "Path";
	private static String propertlyApiPath = "ApiPath";
	public static String PROTOCOL;
	public static String HOST;
	public static int PORT;
	public static String PATH;
	public static String API_PATH;
	private static String testRailListenerPropertiesFile = "TestRailListener.properties";
	private static String propertyUserEmail = "TestRail.User.Email";
	private static String propertyUserApiKey = "TestRail.User.APIKEY";
	private static String propertyProjectID = "TestRail.ProjectID";
	public static String USER;
	public static String API_KEY;
	public static String PROJECT_ID;

	public static void init() {
		if (!initComplete) {
			initComplete = true;
			if (loadTestRailCommonProperties()) {
				loadTestRailListenerProperties();
			}
		}
	}

	private static boolean loadTestRailCommonProperties() {
		boolean commonPropLoaded = false;
		try {
			prop1.load(TestRailListener.class.getClassLoader().getResourceAsStream(testRailCommonPropertiesFile));
			PROTOCOL = prop1.getProperty(propertyProtocol);
			HOST = prop1.getProperty(propertyHost);
			PORT = Integer.parseInt(prop1.getProperty(propertyPort));
			PATH = prop1.getProperty(propertyPath);
			API_PATH = prop1.getProperty(propertlyApiPath);
			commonPropLoaded = true;

		} catch (Exception e) {
			LogUtilities.logSevereMessage("Unable to load '" + testRailCommonPropertiesFile
					+ "' please check that this file in your classpath (src/test/resources).");
		}
		return commonPropLoaded;
	}

	private static void loadTestRailListenerProperties() {
		try {
			prop2.load(TestRailListener.class.getClassLoader().getResourceAsStream(testRailListenerPropertiesFile));
			USER = prop2.getProperty(propertyUserEmail);
			API_KEY = prop2.getProperty(propertyUserApiKey);
			PROJECT_ID = prop2.getProperty(propertyProjectID);
			if (!new ConfigsRequest().isHealthCheckPassed()) {
				LogUtilities.logWarningMessage(String.format("Authentication failed, check your '"
						+ testRailListenerPropertiesFile + "' settings. Now we have " + propertyUserEmail + "=%s, "
						+ propertyUserApiKey + "=%s, " + propertyProjectID + "=%s", USER, API_KEY, PROJECT_ID));
				return;
			}
			initSuccessful = true;
		} catch (Exception e) {
			LogUtilities.logWarningMessage("Unable to load '" + testRailListenerPropertiesFile
					+ "' please check that this file in your classpath (src/test/resources). "
					+ "TestRailReportListener will be disabled.");
		}
	}

	public static boolean isListenerEnabled() {
		return initSuccessful;
	}

}
