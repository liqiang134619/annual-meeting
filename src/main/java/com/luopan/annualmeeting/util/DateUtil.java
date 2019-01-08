package com.luopan.annualmeeting.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期-时间工具类
 */
public final class DateUtil {

  public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
  public static final String DATE_PATTERN = "yyyy-MM-dd";
  public static final String TIME_PATTERN = "HH:mm:ss";

  /**
   * 时间转为字符串
   * @param date
   * @return
   */
  public static String time2Str(Date date, String pattern) {
    if (date == null || pattern == null) {
      return null;
    }
    LocalDateTime localDateTime = date2LocalDateTime(date);
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
    return dateTimeFormatter.format(localDateTime);
  }

  /**
   * 时间转为datetime字符串
   * @param date
   * @return
   */
  public static String time2DateTimeStr(Date date) {
    if (date == null) {
      return null;
    }
    return time2Str(date, DATE_TIME_PATTERN);
  }

  /**
   * date转为localDateTime
   * @param date
   * @return
   */
  public static LocalDateTime date2LocalDateTime(Date date) {
    if (date == null) {
      return null;
    }
    Instant instant = date.toInstant();
    ZoneId zoneId = ZoneId.systemDefault();
    LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
    return localDateTime;
  }

  /**
   * 增加秒
   * @param date
   * @param second
   * @return
   */
  public static Date plusSecond(Date date, int second) {
    if (date == null) {
      return null;
    }
    LocalDateTime localDateTime = date2LocalDateTime(date);
    LocalDateTime newLocalDateTime = localDateTime.plusSeconds(second);
    Date newDate = localDateTime2Date(newLocalDateTime);
    return newDate;
  }

  /**
   * localDateTime转为date
   * @param localDateTime
   * @return
   */
  public static Date localDateTime2Date(LocalDateTime localDateTime) {
    if (localDateTime == null) {
      return null;
    }
    ZoneId zoneId = ZoneId.systemDefault();
    ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
    Date date = Date.from(zonedDateTime.toInstant());
    return date;
  }

}
