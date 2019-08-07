package com.project.test.testrailintegration.api.client.method;

import com.project.test.testrailintegration.api.model.Test;
import com.project.test.testrailintegration.api.model.TestResults;
import com.google.gson.Gson;

public class AddResultsRequest extends AbstractRailRequest {

  private static final String REQUEST_PATTERN = "add_results_for_cases/%s";

  public Test[] addResultsToRun(TestResults testResults, int runId) {
    String body = executePost(String.format(REQUEST_PATTERN, runId), testResults);
    return new Gson().fromJson(body, Test[].class);
  }
}