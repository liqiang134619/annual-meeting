package com.luopan.annualmeeting.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Created by lujw on 2019/1/17.
 */
public abstract class AbstractRedisUtil {

  @Autowired
  protected StringRedisTemplate stringRedisTemplate;

  protected RedisSerializer<String> getStringSerializer() {
    return stringRedisTemplate.getStringSerializer();
  }

  protected <T> RedisSerializer<T> getJsonSerializer(Class<T> clazz) {
    Jackson2JsonRedisSerializer<T> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(
        clazz);
    jackson2JsonRedisSerializer.setObjectMapper(getObjectMapper());
    return jackson2JsonRedisSerializer;
  }

  protected RedisSerializer<Object> getJsonSerializer() {
    return getJsonSerializer(Object.class);
  }

  private ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
    objectMapper.enableDefaultTyping(DefaultTyping.NON_FINAL);
    return objectMapper;
  }

  protected byte[] rawKey(String key) {
    if (key == null) {
      return null;
    }
    return getStringSerializer().serialize(key);
  }

  protected byte[][] rawKey(String... keys) {
    if (keys == null) {
      return null;
    }
    byte[][] bytes = new byte[keys.length][];
    for (int i = 0, len = keys.length; i < len; i++) {
      bytes[i] = rawKey(keys[i]);
    }
    return bytes;
  }

  protected byte[] rawValue(Object value) {
    if (value == null) {
      return null;
    }
    return getJsonSerializer().serialize(value);
  }

  protected byte[][] rawValue(Object... values) {
    if (values == null) {
      return null;
    }
    byte[][] bytes = new byte[values.length][];
    for (int i = 0; i < values.length; i++) {
      bytes[i] = rawValue(values[i]);
    }
    return bytes;
  }

  protected String deserializeKey(byte[] bytes) {
    if (bytes == null) {
      return null;
    }
    return getStringSerializer().deserialize(bytes);
  }

  protected String deserializeStringValue(byte[] bytes) {
    if (bytes == null) {
      return null;
    }
    String deserialize = getStringSerializer().deserialize(bytes);
    return deserialize != null ? deserialize : new String(bytes);
  }

  protected <T> T deserializeValue(byte[] bytes, Class<T> clazz) {
    if (bytes == null) {
      return null;
    }
    if (String.class.equals(clazz)) {
      return (T) deserializeStringValue(bytes);
    }
    RedisSerializer<T> jsonSerializer = getJsonSerializer(clazz);
    return jsonSerializer.deserialize(bytes);
  }

  protected <T> Set<T> deserializeSetValues(Set<byte[]> values, Class<T> clazz) {
    if (values == null) {
      return null;
    }
    RedisSerializer<T> redisSerializer = getJsonSerializer(clazz);
    return values.stream().map(it -> redisSerializer.deserialize(it))
        .collect(Collectors.toSet());
  }

  protected <K, V> Map<K, V> deserializeMapValues(Map<byte[], byte[]> values, Class<K> kClass,
      Class<V> vClass) {
    if (values == null) {
      return null;
    }
    RedisSerializer<K> kRedisSerializer = getJsonSerializer(kClass);
    RedisSerializer<V> vRedisSerializer = getJsonSerializer(vClass);
    HashMap<K, V> kvHashMap = new HashMap<>();
    values.forEach((kBytes, vBytes) -> kvHashMap
        .put(kRedisSerializer.deserialize(kBytes), vRedisSerializer.deserialize(vBytes)));
    return kvHashMap;
  }

  protected <T> List<T> deserializeListValues(List<byte[]> values, Class<T> clazz) {
    if (values == null) {
      return null;
    }
    RedisSerializer<T> redisSerializer = getJsonSerializer(clazz);
    return values.stream().map(it -> redisSerializer.deserialize(it))
        .collect(Collectors.toList());
  }

  protected <T> T execute(RedisCallback<T> callback) {
    return execute(callback, true);
  }

  protected <T> T execute(RedisCallback<T> callback, boolean b) {
    return this.stringRedisTemplate.execute(callback, b);
  }

}
