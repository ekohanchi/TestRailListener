package com.project.test.testrailintegration.api.client.method;

import org.apache.log4j.Logger;

import com.project.test.testrailintegration.config.Config;

/**
 * Request now used just to verify the credentials.
 */
public class ConfigsRequest extends AbstractRailRequest {

  private static final Logger logger = Logger.getLogger(ConfigsRequest.class);
  private static final String REQUEST_PATTERN = "get_configs/%s";

  public boolean isHealthCheckPassed() {
    try {
      String body = executeGet(String.format(REQUEST_PATTERN, Config.PROJECT_ID));
      return body != null && body.length() > 0;
    } catch (Throwable e) {
      logger.trace("Health check to test rail failed", e);
      return false;
    }
  }
}