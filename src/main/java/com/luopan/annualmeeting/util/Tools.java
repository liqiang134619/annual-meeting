package com.luopan.annualmeeting.util;

import javax.servlet.http.HttpServletRequest;

public final class Tools {

  private Tools() {

  }

  public static Integer getInt(String str) {
    try {
      Integer num = Integer.parseInt(str);
      return num;
    } catch (Exception e) {
    }
    return null;
  }

  public static Integer getInt(String str, int defaultNum) {
    Integer num = getInt(str);
    if (num == null) {
      return defaultNum;
    }
    return num;
  }

  public static Long getLong(String str) {
    try {
      Long num = Long.parseLong(str);
      return num;
    } catch (Exception e) {
    }
    return null;
  }

  public static Long getLong(String str, long defaultNum) {
    Long num = getLong(str);
    if (num == null) {
      return defaultNum;
    }
    return num;
  }

  public static Long getLongFromRequest(HttpServletRequest request, String name) {
    if (request == null) {
      return null;
    }
    String stringValue = request.getHeader(name);
    if (stringValue == null) {
      stringValue = request.getParameter(name);
    }
    return getLong(stringValue);
  }

}
