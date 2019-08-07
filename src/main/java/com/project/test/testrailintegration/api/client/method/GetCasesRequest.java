package com.project.test.testrailintegration.api.client.method;

import com.project.test.testrailintegration.api.model.TestCase;
import com.project.test.testrailintegration.config.Config;
import com.google.gson.Gson;

public class GetCasesRequest extends AbstractRailRequest {

  private static final String REQUEST_PATTERN = "get_cases/%s";

  public TestCase[] getProjectCases() {
    String body = executeGet(String.format(REQUEST_PATTERN, Config.PROJECT_ID));
    return new Gson().fromJson(body, TestCase[].class);
  }
}