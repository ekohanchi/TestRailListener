package com.project.test.testrailintegration.api.client.method;

import com.project.test.testrailintegration.api.model.TestRun;

public class CloseTestRunRequest  extends AbstractRailRequest {

  private static final String REQUEST_PATTERN = "close_run/%s";

  public void closeRun(TestRun run) {
    executePost(String.format(REQUEST_PATTERN, run.id), run);
  }
}