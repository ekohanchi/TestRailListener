package com.project.test.testrailintegration.api.model;

public class TestCase implements Comparable<TestCase> {

	public int id;
	public int custom_automation;

	public TestCase(int caseId, int automation_id) {
		this.id = caseId;
		this.custom_automation = automation_id;
	}

	@Override
	public int compareTo(TestCase another) {
		return Integer.compare(id, another.id);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof TestCase && compareTo((TestCase) obj) == 0;
	}

	@Override
	public int hashCode() {
		return id;
	}
}