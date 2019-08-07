package com.project.test.testrailintegration.api.client.method;

import com.project.test.testrailintegration.api.model.TestRun;
import com.project.test.testrailintegration.config.Config;
import com.google.gson.Gson;

public class AddTestRunRequest extends AbstractRailRequest {

  private static final String REQUEST_PATTERN = "add_run/%s";

  public TestRun createRun(TestRun run) {
    String body = executePost(String.format(REQUEST_PATTERN, Config.PROJECT_ID), run);
    return new Gson().fromJson(body, TestRun.class);
  }
}