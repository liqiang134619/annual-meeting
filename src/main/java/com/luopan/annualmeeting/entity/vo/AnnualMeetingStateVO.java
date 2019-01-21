package com.luopan.annualmeeting.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AnnualMeetingStateVO implements Serializable {

  private static final long serialVersionUID = 6618772059537987926L;

  // 年会开始时间
  private Date startTime;

  // 年会结束时间
  private Date endTime;

  // 标题
  private String title;

  // 大屏地址
  private String webUrl;

  // 手机端地址
  private String mobileUrl;

  @JsonIgnore
  private Long companyId;

  // 消息间隔时间
  private Integer messageTaskInterval;

  // 消息数
  private Integer messageTaskNum;

  // 消息审批标志
  private Integer messageCheckState;

  // 单人投票次数
  private Integer personVoteNum;

  // 背景图片地址
  private String backgroundImageUrl;

  // 奖品背景图片地址
  private String rewardBackgroundImageUrl;

}
