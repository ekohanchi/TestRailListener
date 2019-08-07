package com.project.test.testrailintegration.exception;

public class ApiException extends RuntimeException {

  private static final long serialVersionUID = -4060310478108970038L;

  public ApiException(String message) {
    super(message);
  }

  public ApiException(Throwable cause) {
    super(cause);
  }
}
