package com.project.test.testrailintegration.core;

import com.project.test.testrailintegration.config.Config;

public class CommentReporter {

  private static StringBuilder commentBuilder = new StringBuilder();

  public static void clear() {
    commentBuilder.setLength(0);
  }

  public static void addComment(String comment) {
    if (Config.isListenerEnabled()) {
      commentBuilder.append(comment).append("\n");
    }
  }

  public static String getReport() {
    return commentBuilder.toString();
  }
}
