package com.project.test.testrailintegration.api.model;

import java.util.ArrayList;

public class TestResults {

  public final ArrayList<TestResult> results = new ArrayList<>();

  public class TestResult implements Comparable<TestResult> {

    private int case_id;
    private int status_id;
    private String elapsed;

    public TestResult(ResultStatus status, int caseId, String elapsedTime) {
      this.case_id = caseId;
      this.status_id = status.getStatusId();
      this.elapsed = elapsedTime;
    }

    public int getCase_id() {
      return case_id;
    }

    public void setCase_id(int case_id) {
      this.case_id = case_id;
    }

    public int getStatus_id() {
      return status_id;
    }

    public void setStatus_id(int status_id) {
      this.status_id = status_id;
    }

    public String getElapsed() {
		return elapsed;
	}

	public void setElapsed(String elapsed) {
		this.elapsed = elapsed;
	}

	@Override
    public int compareTo(TestResult another) {
      return Integer.compare(case_id, another.case_id);
    }

    @Override
    public boolean equals(Object obj) {
      return obj instanceof TestResult && compareTo((TestResult) obj) == 0;
    }

    @Override
    public int hashCode() {
      return case_id * 31 + status_id;
    }
  }
}