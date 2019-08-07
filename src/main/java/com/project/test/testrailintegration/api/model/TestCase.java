package com.project.test.testrailintegration.api.model;

public class TestCase implements Comparable<TestCase> {

  public int id;
  public int type_id;

  public TestCase(int caseId, int type_id) {
    this.type_id = type_id;
    this.id = caseId;
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