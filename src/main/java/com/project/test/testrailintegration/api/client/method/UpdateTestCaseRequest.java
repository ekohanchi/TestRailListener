package com.project.test.testrailintegration.api.client.method;

import com.project.test.testrailintegration.api.model.TestCase;
import com.project.test.testrailintegration.api.model.TestCaseAutomation;

public class UpdateTestCaseRequest extends AbstractRailRequest {

	private static final String REQUEST_PATTERN = "update_case/%s";
	private static final int AUTOMATED_ID = TestCaseAutomation.AUTOMATED.getValue();

	public void markCaseAsAutomated(int testCaseId) {
		TestCase testCase = new TestCase(testCaseId, AUTOMATED_ID);
		executePost(String.format(REQUEST_PATTERN, testCase.id), testCase);
	}
}