package com.luopan.annualmeeting.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

/**
 * Created by lujw on 2019/1/16.
 */
@Slf4j
public final class BeanUtil {

  public static <T> T copyProperties(Object source, Class<T> targetClass) {
    T t = null;
    if (source != null) {
      try {
        t = targetClass.newInstance();
        BeanUtils.copyProperties(source, t);
      } catch (Exception e) {
        log.error("复制对象出错", e);
        e.printStackTrace();
        throw new RuntimeException("复制对象出错", e);
      }
    }
    return t;
  }

  public static <T> List<T> copyProperties(List<?> source, Class<T> targetClass) {
    List<T> list = null;
    if (source != null) {
      list = source.stream().map(obj -> copyProperties(obj, targetClass))
          .collect(Collectors.toList());
    }
    return list;
  }

  public static boolean isEmpty(Collection<?> list) {
    return list == null || list.isEmpty();
  }

  public static boolean isEmpty(Map map) {
    return map == null || map.isEmpty();
  }

  public static boolean isNotEmpty(Collection<?> list) {
    return !isEmpty(list);
  }

  public static boolean isNotEmpty(Map map) {
    return !isEmpty(map);
  }

}
