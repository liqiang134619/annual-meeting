package com.luopan.annualmeeting.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.text.SimpleDateFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public final class JsonUtil {

  private JsonUtil() {

  }

  private static ObjectMapper objectMapper = new ObjectMapper();

  static {
    // 对象字段全部列入
    objectMapper.setSerializationInclusion(Include.ALWAYS);

    // 忽略空bean转json的错误
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    // 取消默认转换timestamps形式
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);

    // 统一日期格式yyyy-MM-dd HH:mm:ss
    objectMapper.setDateFormat(new SimpleDateFormat(DateUtil.DATE_TIME_PATTERN));

    // 忽略在json字符串中存在,但是在java对象中不存在对应属性的情况
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  /**
   * Object转json字符串
   */
  public static <T> String obj2String(T obj) {
    if (obj == null) {
      return null;
    }
    try {
      return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
    } catch (Exception e) {
      log.info("Parse object to String error");
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Object转json字符串并格式化美化
   */
  public static <T> String obj2StringPretty(T obj) {
    if (obj == null) {
      return null;
    }
    try {
      return obj instanceof String ? (String) obj
          : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    } catch (Exception e) {
      log.info("Parse object to String error");
      e.printStackTrace();
      return null;
    }
  }

  /**
   * string转object
   *
   * @param str json字符串
   * @param clazz 被转对象class
   */
  @SuppressWarnings("unchecked")
  public static <T> T string2Obj(String str, Class<T> clazz) {
    if (StringUtils.isEmpty(str) || clazz == null) {
      return null;
    }
    try {
      return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
    } catch (IOException e) {
      log.info("Parse String to Object error");
      e.printStackTrace();
      return null;
    }
  }

  /**
   * string转object
   *
   * @param str json字符串
   * @param typeReference 被转对象引用类型
   */
  @SuppressWarnings("unchecked")
  public static <T> T string2Obj(String str, TypeReference<T> typeReference) {
    if (StringUtils.isEmpty(str) || typeReference == null) {
      return null;
    }
    try {
      return (T) (typeReference.getType().equals(String.class) ? str
          : objectMapper.readValue(str, typeReference));
    } catch (IOException e) {
      log.info("Parse String to Object error");
      e.printStackTrace();
      return null;
    }
  }

  /**
   * string转object 用于转为集合对象
   *
   * @param str json字符串
   * @param collectionClass 被转集合class
   * @param elementClasses 被转集合中对象类型class
   */
  public static <T> T string2Obj(String str, Class<?> collectionClass, Class<?>... elementClasses) {
    JavaType javaType = objectMapper.getTypeFactory()
        .constructParametricType(collectionClass, elementClasses);
    try {
      return objectMapper.readValue(str, javaType);
    } catch (IOException e) {
      log.info("Parse String to Object error");
      e.printStackTrace();
      return null;
    }
  }

}
