package com.luopan.annualmeeting.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class CommConfig {

  @Value("${person.vote.num}")
  private int personVoteNum;

  @Value("${message.task.interval}")
  private int messageTaskInterval;

  @Value("${message.task.num}")
  private int messageTaskNum;

  @Value("${wechat.appId}")
  private String wechatAppId;

  @Value("${wechat.appSecret}")
  private String wechatAppSecret;

  @Value("${vote.task.interval}")
  private int voteTaskInterval;

}
