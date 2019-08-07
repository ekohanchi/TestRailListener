package com.project.test.testrailintegration.api.model;

public enum TestCaseAutomation {
	AUTOMATED(1), NOTAUTOMATED(2), NOTAUTOMATABLE(3);

	private int automation_id;

	TestCaseAutomation(int id) {
		this.automation_id = id;
	}

	public int getValue() {
		return automation_id;
	}
}