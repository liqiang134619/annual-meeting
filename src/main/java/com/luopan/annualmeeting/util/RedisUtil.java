package com.luopan.annualmeeting.util;

import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@SuppressWarnings("unchecked")
public final class RedisUtil {

  private RedisUtil() {

  }

  @Autowired
  private RedisTemplate redisTemplate;

  @Autowired
  private StringRedisTemplate stringRedisTemplate;

  /**
   * 删除缓存
   *
   * @param key 可以传一个值 或多个
   */
  public void del(String... key) {
    if (key != null && key.length > 0) {
      if (key.length == 1) {
        redisTemplate.delete(key[0]);
      } else {
        redisTemplate.delete(CollectionUtils.arrayToList(key));
      }
    }
  }

  /**
   * 普通缓存获取
   */
  public Object get(String key) {
    return key == null ? null : redisTemplate.opsForValue().get(key);
  }


  /**
   * 普通缓存放入
   */
  public void set(String key, Object value) {
    redisTemplate.opsForValue().set(key, value);
  }

  /**
   * 缓存获取string
   */
  public String getString(String key) {
    return key == null ? null : stringRedisTemplate.opsForValue().get(key);
  }

  /**
   * 缓存放入string
   */
  public void setString(String key, String value) {
    stringRedisTemplate.opsForValue().set(key, value);
  }

  //================set=================

  /**
   * 根据key获取Set中的所有值
   */
  public Set<Object> sGet(String key) {
    return redisTemplate.opsForSet().members(key);
  }

  /**
   * 根据value从一个set中查询,是否存在
   */
  public boolean sHasKey(String key, Object value) {
    return redisTemplate.opsForSet().isMember(key, value);
  }

  /**
   * 将数据放入set缓存
   */
  public void sSet(String key, Object... values) {
    redisTemplate.opsForSet().add(key, values);
  }

  /**
   * 获取set缓存的长度
   *
   * @param key 键
   */
  public long sGetSetSize(String key) {
    return redisTemplate.opsForSet().size(key);
  }

  /**
   * 移除值为value的
   *
   * @param key 键
   * @param values 值 可以是多个
   * @return 移除的个数
   */
  public void setRemove(String key, Object... values) {
    redisTemplate.opsForSet().remove(key, values);
  }

  /**
   * 判断是否包含指定值
   * @param key 键
   * @param value 值
   * @return 是否包含
   */
  public boolean sContain(String key, Object value) {
    return redisTemplate.opsForSet().isMember(key, value);
  }


  //====================================list=============================

  /**
   * 获取list缓存的内容
   *
   * @param key 键
   * @param start 开始
   * @param end 结束  0 到 -1代表所有值
   */
  public List<Object> lGet(String key, long start, long end) {
    return redisTemplate.opsForList().range(key, start, end);
  }

  /**
   * 获取list缓存的长度
   *
   * @param key 键
   */
  public long lGetListSize(String key) {
    return redisTemplate.opsForList().size(key);
  }

  /**
   * 通过索引 获取list中的值
   *
   * @param key 键
   * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
   */
  public Object lGetIndex(String key, long index) {
    return redisTemplate.opsForList().index(key, index);
  }

  /**
   * 将list放入缓存
   *
   * @param key 键
   * @param value 值
   */
  public void lSet(String key, Object value) {
    redisTemplate.opsForList().rightPush(key, value);
  }

  /**
   * 将list放入缓存
   *
   * @param key 键
   * @param value 值
   */
  public void lSet(String key, List<Object> value) {
    redisTemplate.opsForList().rightPushAll(key, value);
  }

  /**
   * 根据索引修改list中的某条数据
   *
   * @param key 键
   * @param index 索引
   * @param value 值
   */
  public void lUpdateIndex(String key, long index, Object value) {
    redisTemplate.opsForList().set(key, index, value);
  }

  /**
   * 移除N个值为value
   *
   * @param key 键
   * @param count 移除多少个
   * @param value 值
   * @return 移除的个数
   */
  public void lRemove(String key, long count, Object value) {
    redisTemplate.opsForList().remove(key, count, value);
  }

}
