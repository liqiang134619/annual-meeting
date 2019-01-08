package com.luopan.annualmeeting;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.luopan.annualmeeting.websocket.ServerManageWebSocket;
import com.luopan.annualmeeting.websocket.WebSocket;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class AnnualMeetingApplication {

  /**
   * websocket
   * @return
   */
  @Bean
  public ServerEndpointExporter serverEndpointExporter() {
    return new ServerEndpointExporter();
  }

  /**
   * redis
   * @param redisConnectionFactory
   * @return
   */
  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory);

    // 使用Jackson2JsonRedisSerialize 替换默认序列化
    Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
    objectMapper.enableDefaultTyping(DefaultTyping.NON_FINAL);

    jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    redisTemplate.setKeySerializer(stringRedisSerializer);
    redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
    redisTemplate.setHashKeySerializer(stringRedisSerializer);
    redisTemplate.setHashValueSerializer(stringRedisSerializer);
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }

  public static void main(String[] args) {
    ConfigurableApplicationContext applicationContext = SpringApplication
        .run(AnnualMeetingApplication.class, args);
    // 解决websocket属性无法注入的问题
    WebSocket.fillAutowireProperty(applicationContext);
    ServerManageWebSocket.fillAutowireProperty(applicationContext);
  }

}

