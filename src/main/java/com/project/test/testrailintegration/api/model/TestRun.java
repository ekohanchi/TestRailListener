package com.project.test.testrailintegration.api.model;

import java.util.HashSet;
import java.util.Set;

public class TestRun {

  public int id;
  public String name;
  public String description;
  public Set<Integer> case_ids;
  public int project_id;
  public boolean include_all = false;

  public TestRun(String name) {
    this.name = name;
    //this.description = description;
    this.case_ids = new HashSet<>();
  }
  
  public TestRun(int id, String name) {
	  this.id = id;
	  this.name = name;
	  this.case_ids = new HashSet<>();
  }
}